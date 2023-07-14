package io.github.toberocat.guiengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.GuiComponentBuilder;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
import io.github.toberocat.guiengine.interpreter.GuiInterpreter;
import io.github.toberocat.guiengine.interpreter.InterpreterManager;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Created: 05/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public final class GuiEngineApi {
    public static final Map<String, GuiEngineApi> APIS = new HashMap<>();
    public static final @NotNull Map<UUID, GuiContext> LOADED_CONTEXTS = new HashMap<>();
    public static final @NotNull FileFilter DEFAULT_GUI_FILTER = file -> file.getName().endsWith(".gui");
    public static final List<SimpleModule> SHARED_MODULES = new LinkedList<>();
    public static final Map<String, Class<? extends GuiComponent>> SHARED_COMPONENT_ID_MAPS = new HashMap<>();

    private final @NotNull ObjectMapper xmlMapper = new XmlMapper();
    private final @NotNull FileFilter guiFilter;
    private final @NotNull Map<String, Class<? extends GuiComponent>> componentIdMap =
            new HashMap<>(SHARED_COMPONENT_ID_MAPS);


    private final @NotNull GuiEngineApiPlugin plugin;
    private final @NotNull File guiFolder;
    private @NotNull Set<String> availableGuis;
    private final @NotNull String id;


    public GuiEngineApi(@NotNull String id, @NotNull File guiFolder) {
        this(id, guiFolder, DEFAULT_GUI_FILTER);
    }

    public GuiEngineApi(@NotNull String id, @NotNull File guiFolder, @NotNull FileFilter guiFilter) {
        this.guiFilter = guiFilter;
        this.guiFolder = guiFolder;
        this.plugin = GuiEngineApiPlugin.getPlugin();
        this.availableGuis = new HashSet<>();
        this.xmlMapper.registerModules(SHARED_MODULES);
        this.id = id;

        if (!guiFolder.exists() && !guiFolder.mkdirs()) {
            Bukkit.getLogger().severe("Couldn't create gui folder " + guiFolder.getAbsolutePath());
            return;
        }
        APIS.put(id, this);
    }

    public static <T extends GuiComponent, B extends GuiComponentBuilder> void registerSharedFactory(@NotNull String id,
                                                                                                     @NotNull Class<T> clazz,
                                                                                                     @NotNull Class<B> builderClazz) {
        SHARED_COMPONENT_ID_MAPS.put(id, clazz);
        SimpleModule module = new SimpleModule();
        module.addSerializer(clazz, new GuiComponentSerializer<>(clazz));
        module.addDeserializer(clazz, new GuiComponentDeserializer<>(builderClazz));
        SHARED_MODULES.add(module);
    }

    public <T extends GuiComponent, B extends GuiComponentBuilder> void registerFactory(@NotNull String id,
                                                                                        @NotNull Class<T> clazz,
                                                                                        @NotNull Class<B> builderClazz) {
        componentIdMap.put(id, clazz);
        SimpleModule module = new SimpleModule();
        module.addSerializer(clazz, new GuiComponentSerializer<>(clazz));
        module.addDeserializer(clazz, new GuiComponentDeserializer<>(builderClazz));
        xmlMapper.registerModules(module);
    }

    public void reload() throws GuiIORuntimeException {
        availableGuis = listGuis(guiFolder)
                .stream()
                .map(this::guiIdFromFile)
                .collect(Collectors.toSet());

        for (String gui : new HashSet<>(availableGuis))
            validateGui(gui);

        Bukkit.getConsoleSender().sendMessage("[GuiEngineAPI] §cPlease note when comparing the " +
                "results that the first few result will be slow. " +
                "Read more about the JIT compiler here: https://www.baeldung.com/graal-java-jit-compiler");
    }

    public @NotNull GuiContext openGui(@NotNull Player player, @NotNull String guiId)
            throws GuiNotFoundRuntimeException, GuiIORuntimeException {
        return openGui(player, guiId, getGuiPlaceholders(player));
    }

    public @NotNull GuiContext openGui(@NotNull Player player, @NotNull String guiId,
                                       @NotNull Map<String, String> placeholders)
            throws GuiNotFoundRuntimeException, GuiIORuntimeException {

        XmlGui xmlGui = loadXmlGui(placeholders, guiId);
        GuiInterpreter interpreter = plugin.getInterpreterManager()
                .getInterpreter(xmlGui.getInterpreter());
        if (interpreter == null)
            throw new GuiIORuntimeException("No interpreter found for " + xmlGui.getInterpreter());

        GuiContext context = interpreter.loadContent(this, player, xmlGui);
        interpreter.getRenderEngine().showGui(context, player, placeholders);
        return context;
    }

    public @NotNull XmlGui loadXmlGui(@NotNull Map<String, String> placeholders, @NotNull String guiId)
            throws GuiNotFoundRuntimeException, GuiIORuntimeException {
        File gui = new File(guiFolder, guiId + ".gui");
        if (!gui.exists())
            throw new GuiNotFoundRuntimeException(guiId);

        try {
            String content = Files.readString(gui.toPath());
            return xmlMapper.readValue(StringSubstitutor.replace(content, placeholders, "%", "%"), XmlGui.class);
        } catch (IOException e) {
            throw new GuiIORuntimeException(e);
        }
    }

    public @NotNull String guiIdFromFile(@NotNull File file) {
        Path gui = file.toPath();
        Path folder = guiFolder.toPath();
        return folder.relativize(gui).toString().split("\\.")[0];
    }

    public @NotNull Set<String> getAvailableGuis() {
        return availableGuis;
    }

    public @NotNull InterpreterManager getInterpreterManager() {
        return plugin.getInterpreterManager();
    }

    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return plugin.getGuiViewManager();
    }

    public @NotNull ObjectMapper getXmlMapper() {
        return xmlMapper;
    }

    public @NotNull FileFilter getGuiFilter() {
        return guiFilter;
    }

    public @NotNull Map<String, Class<? extends GuiComponent>> getComponentIdMap() {
        return componentIdMap;
    }

    private @NotNull List<File> listGuis(@NotNull File folder) {
        File[] files = folder.listFiles();
        List<File> guis = new LinkedList<>();
        if (files == null)
            return guis;

        for (File file : files) {
            if (file.isDirectory()) {
                guis.addAll(listGuis(file));
                continue;
            }

            if (!guiFilter.accept(file))
                continue;

            guis.add(file);
        }
        return guis;
    }

    private void validateGui(@NotNull String gui) throws GuiIORuntimeException {
        long total = 0;
        long delta;

        try {
            Bukkit.getConsoleSender().sendMessage("[GuiEngineAPI] Validating " + gui + ".gui");
            VirtualPlayer virtualPlayer = new VirtualPlayer();

            long now = System.currentTimeMillis();
            XmlGui xmlGui = loadXmlGui(getGuiPlaceholders(virtualPlayer), gui);

            delta = System.currentTimeMillis() - now;
            Bukkit.getConsoleSender().sendMessage(String
                    .format("[GuiEngineAPI] Took %dms parsing %s", delta, gui));
            total += delta;

            now = System.currentTimeMillis();
            GuiInterpreter interpreter = plugin.getInterpreterManager()
                    .getInterpreter(xmlGui.getInterpreter());
            if (interpreter == null)
                throw new GuiIORuntimeException("No interpreter found for " + xmlGui.getInterpreter());
            GuiContext context = interpreter.loadContent(this, virtualPlayer, xmlGui);

            delta = System.currentTimeMillis() - now;
            Bukkit.getConsoleSender().sendMessage(String
                    .format("[GuiEngineAPI] Took %dms creating a context for %s",
                            delta, gui));
            total += delta;

            now = System.currentTimeMillis();
            CompletableFuture<Void> renderTask = CompletableFuture.runAsync(() -> {
                ItemStack[][] virtualInventory = new ItemStack[xmlGui.getHeight()][xmlGui.getWidth()];
                context.setViewer(virtualPlayer);
                context.setInventory(new VirtualInventory(6, () -> {
                }));
                context.componentsDescending().forEach(event -> event.onViewInit(new HashMap<>()));
                interpreter.getRenderEngine().renderGui(virtualInventory, context, virtualPlayer);
            });

            try {
                renderTask.get(1, TimeUnit.SECONDS);
                delta = System.currentTimeMillis() - now;
                Bukkit.getConsoleSender().sendMessage(String
                        .format("[GuiEngineAPI] Took %dms rendering to a virtual player %s",
                                delta, gui));
            } catch (TimeoutException | StackOverflowError e) {
                renderTask.cancel(true);
                plugin.getLogger().severe("Stopped rendering gui to virtual " +
                        "player. The render process is very likely to render an infinite long period");
                throw e;
            }

            total += delta;
            Bukkit.getConsoleSender().sendMessage(String
                    .format("[GuiEngineAPI] §aTook in total %dms§a to get %s displayed to the virtual player",
                            total, gui));
        } catch (InvalidGuiComponentException e) {
            availableGuis.remove(gui);
            plugin.getLogger().severe(String.format("%s.gui has a invalid component. %s", gui, e.getMessage()));
        } catch (Throwable e) {
            availableGuis.remove(gui);
            e.printStackTrace();
            plugin.getLogger().severe("The gui couldn't get rendered to an " +
                    "virtual player. Please take a look at it");
            throw new GuiIORuntimeException(e);
        }
    }

    private @NotNull Map<String, String> getGuiPlaceholders(@NotNull Player viewer) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("viewer", viewer.getUniqueId().toString());
        return placeholders;
    }

    public @NotNull String getId() {
        return id;
    }
}
