package io.github.toberocat.guiengine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.GuiComponentBuilder;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
import io.github.toberocat.guiengine.exception.InvalidGuiFileException;
import io.github.toberocat.guiengine.interpreter.GuiInterpreter;
import io.github.toberocat.guiengine.interpreter.InterpreterManager;
import io.github.toberocat.guiengine.utils.FileUtils;
import io.github.toberocat.guiengine.utils.VirtualInventory;
import io.github.toberocat.guiengine.utils.VirtualPlayer;
import io.github.toberocat.guiengine.view.DefaultGuiViewManager;
import io.github.toberocat.guiengine.xml.GuiComponentDeserializer;
import io.github.toberocat.guiengine.xml.GuiComponentSerializer;
import io.github.toberocat.guiengine.xml.XmlGui;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The `GuiEngineApi` class provides an API to manage and interact with graphical user interfaces (GUIs).
 * It allows you to register and manage various GUI components, load XML-based GUI definitions,
 * and display GUIs to players in the game.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 05/02/2023
 */
public final class GuiEngineApi {
    public static final Map<String, GuiEngineApi> APIS = new HashMap<>();
    public static final @NotNull Map<UUID, GuiContext> LOADED_CONTEXTS = new HashMap<>();
    public static final @NotNull FileFilter DEFAULT_GUI_FILTER = file -> file.getName().endsWith(".gui");
    public static final List<SimpleModule> SHARED_MODULES = new LinkedList<>();
    public static final Pattern GUI_ID_REGEX = Pattern.compile("[^a-zA-Z_-]", Pattern.MULTILINE);
    public static final Map<String, Class<? extends GuiComponent>> SHARED_COMPONENT_ID_MAPS = new HashMap<>();

    private final @NotNull ObjectMapper xmlMapper = new XmlMapper();
    private final @NotNull FileFilter guiFilter;
    private final @NotNull Map<String, Class<? extends GuiComponent>> componentIdMap = new HashMap<>(SHARED_COMPONENT_ID_MAPS);


    private final @NotNull GuiEngineApiPlugin plugin;
    private final @NotNull File guiFolder;
    private @NotNull Set<String> availableGuis;
    private final @NotNull String id;

    /**
     * Constructs a new `GuiEngineApi` instance with the specified ID and GUI folder.
     *
     * @param id        The ID for this `GuiEngineApi` instance.
     * @param guiFolder The folder where GUI files are stored.
     */
    public GuiEngineApi(@NotNull String id, @NotNull File guiFolder) {
        this(id, guiFolder, DEFAULT_GUI_FILTER);
    }

    /**
     * Constructs a new `GuiEngineApi` instance for the specified plugin.
     * The ID will be automatically set to the plugin's name, and the gui folder will be the data-folder/guis
     * <p>
     * This has the advantage that GuiEngine automatically copies the guis folder from the resources
     *
     * @param plugin The plugin this API owns to
     */
    public GuiEngineApi(@NotNull JavaPlugin plugin) throws IOException, URISyntaxException {
        this(plugin.getName(), FileUtils.copyAll(plugin, "guis"));
    }

    /**
     * Constructs a new `GuiEngineApi` instance with the specified ID, GUI folder, and GUI filter.
     *
     * @param id        The ID for this `GuiEngineApi` instance.
     * @param guiFolder The folder where GUI files are stored.
     * @param guiFilter The filter used to determine which files are considered GUI files.
     */
    public GuiEngineApi(@NotNull String id, @NotNull File guiFolder, @NotNull FileFilter guiFilter) {
        this.guiFilter = guiFilter;
        this.guiFolder = guiFolder;
        plugin = GuiEngineApiPlugin.getPlugin();
        availableGuis = new HashSet<>();
        xmlMapper.registerModules(SHARED_MODULES);
        this.id = GUI_ID_REGEX.matcher(id).replaceAll("");

        if (!guiFolder.exists() && !guiFolder.mkdirs()) {
            Bukkit.getLogger().severe("Couldn't create gui folder " + guiFolder.getAbsolutePath());
            return;
        }
        APIS.put(id, this);
    }

    /**
     * Registers a shared GUI component factory with the given ID, class, and builder class.
     * Shared factories can be used across different instances of `GuiEngineApi`.
     *
     * @param id           The ID of the shared GUI component factory.
     * @param clazz        The class representing the GUI component.
     * @param builderClazz The class representing the GUI component builder.
     * @param <T>          The type of the GUI component.
     * @param <B>          The type of the GUI component builder.
     */
    public static <T extends GuiComponent, B extends GuiComponentBuilder> void registerSharedFactory(@NotNull String id, @NotNull Class<T> clazz, @NotNull Class<B> builderClazz) {
        SHARED_COMPONENT_ID_MAPS.put(id, clazz);
        SimpleModule module = new SimpleModule();
        module.addSerializer(clazz, new GuiComponentSerializer<>(clazz));
        module.addDeserializer(clazz, new GuiComponentDeserializer<>(builderClazz));
        SHARED_MODULES.add(module);
    }

    /**
     * Registers a GUI component factory with the given ID, class, and builder class.
     *
     * @param id           The ID of the GUI component factory.
     * @param clazz        The class representing the GUI component.
     * @param builderClazz The class representing the GUI component builder.
     * @param <T>          The type of the GUI component.
     * @param <B>          The type of the GUI component builder.
     */
    public <T extends GuiComponent, B extends GuiComponentBuilder> void registerFactory(@NotNull String id, @NotNull Class<T> clazz, @NotNull Class<B> builderClazz) {
        componentIdMap.put(id, clazz);
        SimpleModule module = new SimpleModule();
        module.addSerializer(clazz, new GuiComponentSerializer<>(clazz));
        module.addDeserializer(clazz, new GuiComponentDeserializer<>(builderClazz));
        xmlMapper.registerModules(module);
    }

    /**
     * Reloads the available GUIs from the GUI folder.
     *
     * @throws GuiIORuntimeException If there is an I/O error while loading or validating GUIs.
     */
    public void reload() throws GuiIORuntimeException {
        availableGuis = listGuis(guiFolder).stream().map(this::guiIdFromFile).collect(Collectors.toSet());

        long[] totals = new long[availableGuis.size()];
        List<String> guisCopy = new ArrayList<>(availableGuis);
        for (int i = 0; i < guisCopy.size(); i++)
            totals[i] = validateGui(guisCopy.get(i));
        double avg = Arrays.stream(totals).average().orElse(Double.NaN);
        double sumSquaredDifferences = Arrays.stream(totals).mapToDouble(total -> (total - avg) * (total - avg)).sum();
        int populationSize = totals.length;
        double variance = sumSquaredDifferences / populationSize;
        double std = Math.sqrt(variance);
        Bukkit.getConsoleSender().sendMessage(String.format("[GuiEngine] §bIt takes §e%.3fms ± %.3fms§b on average to render a gui from §e%s", avg, std, id));
    }

    /**
     * Opens a GUI with the specified ID for the given player, using the default placeholders.
     *
     * @param player The player to whom the GUI should be displayed.
     * @param guiId  The ID of the GUI to open.
     * @return The `GuiContext` representing the opened GUI.
     * @throws GuiNotFoundRuntimeException If the specified GUI ID does not correspond to an existing GUI.
     * @throws GuiIORuntimeException       If there is an I/O error while loading or rendering the GUI.
     */
    public @NotNull GuiContext openGui(@NotNull Player player, @NotNull String guiId) throws GuiNotFoundRuntimeException, GuiIORuntimeException {
        return openGui(player, guiId, getGuiPlaceholders(player));
    }

    /**
     * Opens a GUI with the specified ID for the given player, using the provided placeholders.
     *
     * @param player       The player to whom the GUI should be displayed.
     * @param guiId        The ID of the GUI to open.
     * @param placeholders The map of placeholders to be substituted in the GUI content.
     * @return The `GuiContext` representing the opened GUI.
     * @throws GuiNotFoundRuntimeException If the specified GUI ID does not correspond to an existing GUI.
     * @throws GuiIORuntimeException       If there is an I/O error while loading or rendering the GUI.
     */
    public @NotNull GuiContext openGui(@NotNull Player player, @NotNull String guiId, @NotNull Map<String, String> placeholders) throws GuiNotFoundRuntimeException, GuiIORuntimeException {
        XmlGui xmlGui = loadXmlGui(placeholders, guiId);
        GuiInterpreter interpreter = plugin.getInterpreterManager().getInterpreter(xmlGui.getInterpreter());
        if (null == interpreter) throw new GuiIORuntimeException("No interpreter found for " + xmlGui.getInterpreter());

        GuiContext context = interpreter.loadContent(this, player, xmlGui);
        interpreter.getRenderEngine().showGui(context, player, placeholders);
        return context;
    }

    /**
     * Loads and returns the XML-based GUI definition with the specified ID, substituting the placeholders.
     *
     * @param placeholders The map of placeholders to be substituted in the GUI content.
     * @param guiId        The ID of the GUI to load.
     * @return The loaded `XmlGui` instance representing the GUI.
     * @throws GuiNotFoundRuntimeException If the specified GUI ID does not correspond to an existing GUI.
     * @throws GuiIORuntimeException       If there is an I/O error while loading the GUI.
     */
    public @NotNull XmlGui loadXmlGui(@NotNull Map<String, String> placeholders, @NotNull String guiId) throws GuiNotFoundRuntimeException, GuiIORuntimeException {
        File gui = new File(guiFolder, guiId + ".gui");
        if (!gui.exists()) throw new GuiNotFoundRuntimeException(guiId);

        try {
            String content = Files.readString(gui.toPath());
            return xmlMapper.readValue(StringSubstitutor.replace(content, placeholders, "%", "%"), XmlGui.class);
        } catch (JsonParseException e) {
            throw new InvalidGuiFileException(String.format("Couldn't parse %s.gui. Caused by: %s. Check your guis " + "syntax and validate that it isn't empty", guiId, e.getMessage()));
        } catch (IOException e) {
            throw new GuiIORuntimeException(e);
        }
    }


    /**
     * Converts a file into it's guiId.
     * This takes the extension away and returns the part before
     *
     * @param file The input file
     * @return The guiId of this file
     */
    public @NotNull String guiIdFromFile(@NotNull File file) {
        Path gui = file.toPath();
        Path folder = guiFolder.toPath();
        return folder.relativize(gui).toString().split("\\.")[0];
    }

    /**
     * Returns the set of available GUI IDs within the GUI folder.
     *
     * @return The set of available GUI IDs.
     */
    public @NotNull Set<String> getAvailableGuis() {
        return availableGuis;
    }

    /**
     * Returns the `InterpreterManager` instance associated with this `GuiEngineApi`.
     *
     * @return The `InterpreterManager` instance.
     */
    public @NotNull InterpreterManager getInterpreterManager() {
        return plugin.getInterpreterManager();
    }

    /**
     * Returns the `DefaultGuiViewManager` instance associated with this `GuiEngineApi`.
     *
     * @return The `DefaultGuiViewManager` instance.
     */
    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return plugin.getGuiViewManager();
    }

    /**
     * Returns the `ObjectMapper` used for XML serialization/deserialization of GUI components.
     *
     * @return The `ObjectMapper` instance for XML handling.
     */
    public @NotNull ObjectMapper getXmlMapper() {
        return xmlMapper;
    }

    /**
     * Returns the GUI filter used to determine which files are considered GUI files.
     *
     * @return The `FileFilter` instance representing the GUI filter.
     */
    public @NotNull FileFilter getGuiFilter() {
        return guiFilter;
    }

    /**
     * Returns the map of GUI component IDs and their associated classes for this `GuiEngineApi`.
     *
     * @return The map of GUI component IDs and their associated classes.
     */
    public @NotNull Map<String, Class<? extends GuiComponent>> getComponentIdMap() {
        return componentIdMap;
    }

    private @NotNull List<File> listGuis(@NotNull File folder) {
        File[] files = folder.listFiles();
        List<File> guis = new LinkedList<>();
        if (null == files) return guis;

        for (File file : files) {
            if (file.isDirectory()) {
                guis.addAll(listGuis(file));
                continue;
            }

            if (!guiFilter.accept(file)) continue;

            guis.add(file);
        }
        return guis;
    }

    private long validateGui(@NotNull String gui) throws GuiIORuntimeException {
        long total = 0;
        long delta;

        Logger logger = GuiEngineApiPlugin.getPlugin().getLogger();
        try {
            logger.log(Level.FINE, "Validating " + gui + ".gui");
            VirtualPlayer virtualPlayer = new VirtualPlayer();

            long now = System.currentTimeMillis();
            XmlGui xmlGui = loadXmlGui(getGuiPlaceholders(virtualPlayer), gui);

            delta = System.currentTimeMillis() - now;
            logger.log(Level.FINE, "Took %dms parsing %s", new Object[]{delta, gui});
            total += delta;

            now = System.currentTimeMillis();
            GuiInterpreter interpreter = plugin.getInterpreterManager().getInterpreter(xmlGui.getInterpreter());
            if (null == interpreter)
                throw new GuiIORuntimeException("No interpreter found for " + xmlGui.getInterpreter());
            GuiContext context = interpreter.loadContent(this, virtualPlayer, xmlGui);

            delta = System.currentTimeMillis() - now;
            logger.log(Level.FINE, "Took %dms creating a context for %s", new Object[]{delta, gui});
            total += delta;

            now = System.currentTimeMillis();
            CompletableFuture<Exception> renderTask = CompletableFuture.supplyAsync(() -> {
                ItemStack[][] virtualInventory = new ItemStack[xmlGui.getHeight()][xmlGui.getWidth()];
                context.setViewer(virtualPlayer);
                context.setInventory(new VirtualInventory(6, () -> {
                }));
                try {
                    context.componentsDescending().forEach(event -> event.onViewInit(new HashMap<>()));
                    interpreter.getRenderEngine().renderGui(virtualInventory, context, virtualPlayer);
                } catch (Exception e) {
                    return e;
                }
                return null;
            });

            try {
                @Nullable Exception exception = renderTask.get(1, TimeUnit.SECONDS);
                if (null != exception) throw exception;
                delta = System.currentTimeMillis() - now;
                logger.log(Level.FINE, "Took %dms rendering to a virtual player %s", new Object[]{delta, gui});

            } catch (TimeoutException | StackOverflowError e) {
                renderTask.cancel(true);
                plugin.getLogger().severe("Stopped rendering gui to virtual " + "player. The render process is very likely to render an infinite long period");
                throw e;
            }

            total += delta;
            logger.log(Level.FINE, "§aTook in total %dms§a to get %s displayed to the virtual player", new Object[]{delta, gui});
        } catch (InvalidGuiComponentException e) {
            availableGuis.remove(gui);
            plugin.getLogger().severe(String.format("%s.gui has a invalid component. %s", gui, e.getMessage()));
        } catch (InvalidGuiFileException e) {
            availableGuis.remove(gui);
            plugin.getLogger().severe(e.getMessage());
        } catch (Throwable e) {
            availableGuis.remove(gui);
            e.printStackTrace();
            plugin.getLogger().severe("The gui couldn't get rendered to an " + "virtual player. Please take a look at it");
            throw new GuiIORuntimeException(e);
        }
        return total;
    }

    private @NotNull Map<String, String> getGuiPlaceholders(@NotNull Player viewer) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("viewer", viewer.getUniqueId().toString());
        return placeholders;
    }

    /**
     * Returns the ID of this `GuiEngineApi` instance.
     *
     * @return The ID of this `GuiEngineApi` instance.
     */
    public @NotNull String getId() {
        return id;
    }
}
