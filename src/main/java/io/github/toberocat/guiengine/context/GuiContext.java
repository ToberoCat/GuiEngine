package io.github.toberocat.guiengine.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.event.GuiEventListener;
import io.github.toberocat.guiengine.event.GuiEvents;
import io.github.toberocat.guiengine.event.spigot.GuiCloseEvent;
import io.github.toberocat.guiengine.event.spigot.GuiComponentClickEvent;
import io.github.toberocat.guiengine.event.spigot.GuiComponentDragEvent;
import io.github.toberocat.guiengine.interpreter.GuiInterpreter;
import io.github.toberocat.guiengine.xml.XmlComponent;
import io.github.toberocat.toberocore.action.Action;
import io.github.toberocat.toberocore.util.StreamUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The `GuiContext` class represents a GUI context that holds information about the GUI components and actions for a
 * specific player.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 04/02/2023
 */
public final class GuiContext implements GuiEvents, GuiEventListener {
    private final @NotNull GuiInterpreter interpreter;
    private final @NotNull String title;
    private final int width;
    private final int height;
    private final @NotNull List<GuiComponent> components;
    private final @NotNull Set<Action> localActions;
    private final @NotNull UUID contextId;
    private @Nullable Inventory inventory;
    private @Nullable Player viewer;

    /**
     * Constructs a new `GuiContext` with the provided interpreter, title, width, and height.
     *
     * @param interpreter The `GuiInterpreter` associated with this context.
     * @param title       The title of the GUI context.
     * @param width       The width of the GUI context.
     * @param height      The height of the GUI context.
     */
    public GuiContext(@NotNull GuiInterpreter interpreter, @NotNull String title, int width, int height) {
        this.interpreter = interpreter;
        this.title = title;
        this.width = width;
        this.height = height;
        components = new ArrayList<>();
        localActions = new HashSet<>();
        contextId = UUID.randomUUID();
        GuiEngineApi.LOADED_CONTEXTS.put(contextId, this);
    }

    /**
     * Returns a stream of GUI components in ascending order of their render priority.
     *
     * @return A stream of GUI components in ascending order of their render priority.
     */
    public @NotNull Stream<GuiComponent> componentsAscending() {
        return components.stream().sorted(Comparator.comparingInt(x -> x.renderPriority().ordinal()));
    }

    /**
     * Returns a stream of GUI components in descending order of their render priority.
     *
     * @return A stream of GUI components in descending order of their render priority.
     */
    public @NotNull Stream<GuiComponent> componentsDescending() {
        return components.stream().sorted((x, y) -> -x.renderPriority().compareTo(y.renderPriority()));
    }

    /**
     * Finds a GUI component with the specified ID.
     *
     * @param id The ID of the GUI component to find.
     * @return The GUI component with the specified ID, or null if not found.
     */
    public @Nullable GuiComponent findComponentById(@NotNull String id) {
        return StreamUtils.find(components, x -> x.getId().equals(id));
    }

    /**
     * Finds a GUI component with the specified ID and class type.
     *
     * @param id    The ID of the GUI component to find.
     * @param clazz The class type of the GUI component.
     * @param <T>   The type of the GUI component.
     * @return The GUI component with the specified ID and class type, or null if not found or the class type does not match.
     */
    public @Nullable <T extends GuiComponent> T findComponentById(@NotNull String id, @NotNull Class<T> clazz) {
        GuiComponent component = StreamUtils.find(components, x -> x.getId().equals(id));
        if (component.getClass() != clazz) return null;
        return clazz.cast(component);
    }

    /**
     * Removes a GUI component with the specified ID.
     *
     * @param id The ID of the GUI component to remove.
     */
    public void removeById(@NotNull String id) {
        components.remove(findComponentById(id));
    }

    /**
     * Edits an XML component by ID with the specified edit callback.
     *
     * @param api          The `GuiEngineApi` associated with the XML component.
     * @param id           The ID of the XML component to edit.
     * @param editCallback The callback function to edit the XML component.
     */
    public void editXmlComponentById(@NotNull GuiEngineApi api, @NotNull String id, @NotNull Consumer<XmlComponent> editCallback) {
        GuiComponent component = findComponentById(id);
        int index = components.indexOf(component);
        if (null == component || 0 > index) return;

        XmlComponent xml = interpreter().componentToXml(api, component);
        editCallback.accept(xml);

        GuiComponent newComponent = interpreter().createComponent(xml, api, this);
        components.set(index, newComponent);
    }

    /**
     * Adds a GUI component to this context.
     *
     * @param api       The `GuiEngineApi` associated with the GUI component.
     * @param component The GUI component to add.
     */
    public void add(@NotNull GuiEngineApi api, @NotNull GuiComponent component) {
        components.add(interpreter().bindComponent(component, api, this));
    }

    /**
     * Adds an XML component to this context.
     *
     * @param api       The `GuiEngineApi` associated with the XML component.
     * @param component The XML component to add.
     */
    public void add(@NotNull GuiEngineApi api, @NotNull XmlComponent component) {
        components.add(interpreter().createComponent(component, api, this));
    }

    /**
     * Adds multiple XML components to this context.
     *
     * @param api        The `GuiEngineApi` associated with the XML components.
     * @param components The XML components to add.
     */
    public void add(@NotNull GuiEngineApi api, @NotNull XmlComponent @NotNull ... components) {
        for (XmlComponent component : components) {
            this.components.add(interpreter().createComponent(component, api, this));
        }
    }

    /**
     * Handles the event when a player clicks on a component in the GUI.
     *
     * @param event The `InventoryClickEvent` representing the click event.
     */
    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        interpreter().clickedComponent(event);
        componentsDescending().filter(x -> x.isInComponent(event.getSlot())).filter(x -> !x.hidden()).findFirst().ifPresentOrElse(component -> {
            Bukkit.getPluginManager().callEvent(new GuiComponentClickEvent(this, event, component));
            component.clickedComponent(event);
        }, () -> Bukkit.getPluginManager().callEvent(new GuiComponentClickEvent(this, event, null)));
    }

    /**
     * Handles the event when a player drags an item in the GUI.
     *
     * @param event The `InventoryDragEvent` representing the drag event.
     */
    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        componentsDescending().filter(x -> event.getInventorySlots().stream().anyMatch(x::isInComponent)).filter(x -> !x.hidden()).findFirst().ifPresentOrElse(component -> {
            Bukkit.getPluginManager().callEvent(new GuiComponentDragEvent(this, event, component));
            component.draggedComponent(event);
        }, () -> {
            Bukkit.getPluginManager().callEvent(new GuiComponentDragEvent(this, event, null));
            interpreter().draggedComponent(event);
        });
    }

    /**
     * Handles the event when a player closes the GUI.
     *
     * @param event The `InventoryCloseEvent` representing the close event.
     */
    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        componentsDescending().filter(x -> !x.hidden()).forEachOrdered(x -> x.closedComponent(event));
        Bukkit.getPluginManager().callEvent(new GuiCloseEvent(this, event));
        GuiEngineApi.LOADED_CONTEXTS.remove(contextId);
    }

    /**
     * Returns the associated `GuiInterpreter` for this context.
     *
     * @return The associated `GuiInterpreter`.
     */
    public @NotNull GuiInterpreter interpreter() {
        return interpreter;
    }

    /**
     * Returns the title of the GUI context.
     *
     * @return The title of the GUI context.
     */
    public @NotNull String title() {
        return title;
    }

    /**
     * Returns the width of the GUI context.
     *
     * @return The width of the GUI context.
     */
    public int width() {
        return width;
    }

    /**
     * Returns the height of the GUI context.
     *
     * @return The height of the GUI context.
     */
    public int height() {
        return height;
    }

    /**
     * Returns the list of GUI components in this context.
     *
     * @return The list of GUI components.
     */
    public @NotNull List<GuiComponent> components() {
        return components;
    }

    /**
     * Returns the set of local actions associated with this context.
     *
     * @return The set of local actions.
     */
    public @NotNull Set<Action> getLocalActions() {
        return localActions;
    }

    /**
     * Returns the viewer (player) associated with this GUI context.
     *
     * @return The viewer (player).
     */
    public @Nullable Player viewer() {
        return viewer;
    }

    /**
     * Returns the inventory associated with this GUI context.
     *
     * @return The inventory.
     */
    public @Nullable Inventory inventory() {
        return inventory;
    }

    /**
     * Sets the inventory for this GUI context.
     *
     * @param inventory The inventory to set.
     */
    public void setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Sets the viewer (player) for this GUI context.
     *
     * @param viewer The viewer (player) to set.
     */
    public void setViewer(@Nullable Player viewer) {
        this.viewer = viewer;
    }

    /**
     * Renders the GUI context by updating the inventory with the components' content.
     */
    public void render() {
        ItemStack[][] content2d = new ItemStack[height()][width()];
        interpreter().getRenderEngine().renderGui(content2d, this, viewer);

        ItemStack[] flatContent = inventory.getContents();
        for (int y = 0; y < height(); y++)
            System.arraycopy(content2d[y], 0, flatContent, y * width(), width());
        inventory.setContents(flatContent);
    }

    /**
     * Checks if this `GuiContext` is equal to another object.
     *
     * @param o The object to compare with this `GuiContext`.
     * @return `true` if the objects are equal, otherwise `false`.
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        GuiContext that = (GuiContext) o;

        return new EqualsBuilder().append(width, that.width).append(height, that.height).append(interpreter, that.interpreter).append(title, that.title).append(components, that.components).append(inventory, that.inventory).append(viewer, that.viewer).isEquals();
    }

    /**
     * Generates the hash code for this `GuiContext`.
     *
     * @return The hash code value for this `GuiContext`.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(interpreter).append(title).append(width).append(height).append(components).append(inventory).append(viewer).toHashCode();
    }

    /**
     * Converts this `GuiContext` object to a human-readable string representation.
     *
     * @return A string representation of the `GuiContext`.
     */
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModules(GuiEngineApi.SHARED_MODULES);
            return new StringJoiner(", ", GuiContext.class.getSimpleName() + "[", "]").add("contextId=" + contextId).add("interpreter=" + interpreter.interpreterId()).add("title='" + title + "'").add("width=" + width).add("height=" + height).add("components=" + mapper.writeValueAsString(components)).add("localActions=" + localActions).add("inventory=" + inventory.getSize()).add("viewerName=" + viewer.getDisplayName()).add("viewerID=" + viewer.getUniqueId()).add("inventoryContent=" + Arrays.toString(inventory.getContents())).toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the unique identifier of this `GuiContext`.
     *
     * @return The unique identifier.
     */
    public @NotNull UUID getContextId() {
        return contextId;
    }

    /**
     * Returns the current `GuiContext`.
     *
     * @return The current `GuiContext`.
     */
    @Override
    public @NotNull GuiContext getContext() {
        return this;
    }
}
