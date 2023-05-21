package io.github.toberocat.guiengine.api.context;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.event.GuiEventListener;
import io.github.toberocat.guiengine.api.event.GuiEvents;
import io.github.toberocat.guiengine.api.event.spigot.GuiCloseEvent;
import io.github.toberocat.guiengine.api.event.spigot.GuiComponentClickEvent;
import io.github.toberocat.guiengine.api.event.spigot.GuiComponentDragEvent;
import io.github.toberocat.guiengine.api.interpreter.GuiInterpreter;
import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.xml.XmlComponent;
import io.github.toberocat.toberocore.action.Action;
import io.github.toberocat.toberocore.util.StreamUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
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
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public final class GuiContext implements GuiEvents, GuiEventListener {
    private final @NotNull GuiInterpreter interpreter;
    private final @NotNull String title;
    private final int width;
    private final int height;
    private final @NotNull List<GuiComponent> components;
    private final @NotNull Set<Action> localActions;
    private Inventory inventory;
    private Player viewer;

    /**
     *
     */
    public GuiContext(
            @NotNull GuiInterpreter interpreter,
            @NotNull String title,
            int width,
            int height
    ) {
        this.interpreter = interpreter;
        this.title = title;
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
        this.localActions = new HashSet<>();
    }

    public @NotNull Stream<GuiComponent> componentsAscending() {
        return components.stream()
                .sorted(Comparator.comparingInt(x -> x.renderPriority().ordinal()));
    }

    public @NotNull Stream<GuiComponent> componentsDescending() {
        return components.stream()
                .sorted((x, y) -> -x.renderPriority().compareTo(y.renderPriority()));
    }

    public @Nullable GuiComponent findComponentById(@NotNull String id) {
        return StreamUtils.find(components, x -> x.getId().equals(id));
    }

    public void removeById(@NotNull String id) {
        components.remove(findComponentById(id));
    }

    public void editXmlComponentById(@NotNull GuiEngineApi api,
                                     @NotNull String id,
                                     @NotNull Consumer<XmlComponent> editCallback) {
        GuiComponent component = findComponentById(id);
        int index = components.indexOf(component);
        if (component == null || index < 0)
            return;

        XmlComponent xml = interpreter().componentToXml(api, component);
        editCallback.accept(xml);

        GuiComponent newComponent = interpreter().createComponent(xml, api, this);
        components.set(index, newComponent);
    }

    public void add(@NotNull GuiEngineApi api, @NotNull GuiComponent component) {
        components.add(interpreter().bindComponent(component, api, this));
    }

    public void add(@NotNull GuiEngineApi api, @NotNull XmlComponent component) {
        components.add(interpreter().createComponent(component, api, this));
    }

    public void add(@NotNull GuiEngineApi api, @NotNull XmlComponent... components) {
        for (XmlComponent component : components) {
            this.components.add(interpreter().createComponent(component, api, this));
        }
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        componentsDescending()
                .filter(x -> x.isInComponent(event.getSlot()))
                .findFirst()
                .ifPresentOrElse(component -> {
                            Bukkit.getPluginManager()
                                    .callEvent(new GuiComponentClickEvent(this, event, component));
                            component.clickedComponent(event);
                        },
                        () -> {
                            Bukkit.getPluginManager()
                                    .callEvent(new GuiComponentClickEvent(this, event, null));
                            interpreter().clickedComponent(event);
                        });
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        componentsDescending()
                .filter(x -> event.getInventorySlots()
                        .stream()
                        .anyMatch(x::isInComponent))
                .findFirst()
                .ifPresentOrElse(component -> {
                            Bukkit.getPluginManager()
                                    .callEvent(new GuiComponentDragEvent(this, event, component));
                            component.draggedComponent(event);
                        },
                        () -> {
                            Bukkit.getPluginManager()
                                    .callEvent(new GuiComponentDragEvent(this, event, null));
                            interpreter().draggedComponent(event);
                        });
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        Bukkit.getPluginManager()
                .callEvent(new GuiCloseEvent(this, event));
        componentsDescending().forEachOrdered(x -> x.closedComponent(event));
    }

    public @NotNull GuiInterpreter interpreter() {
        return interpreter;
    }

    public @NotNull String title() {
        return title;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public @NotNull List<GuiComponent> components() {
        return components;
    }

    public @NotNull Set<Action> getLocalActions() {
        return localActions;
    }

    public @Nullable Player viewer() {
        return viewer;
    }

    public @Nullable Inventory inventory() {
        return inventory;
    }

    public void setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
    }

    public void setViewer(@Nullable Player viewer) {
        this.viewer = viewer;
    }

    public void render() {
        ItemStack[][] content2d = new ItemStack[height()][width()];
        interpreter().getRenderEngine().renderGui(content2d, this, viewer);

        ItemStack[] flatContent = inventory.getContents();
        for (int y = 0; y < height(); y++)
            System.arraycopy(content2d[y], 0, flatContent, y * width(), width());
        inventory.setContents(flatContent);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GuiContext that = (GuiContext) o;

        return new EqualsBuilder()
                .append(width, that.width)
                .append(height, that.height)
                .append(interpreter, that.interpreter)
                .append(title, that.title)
                .append(components, that.components)
                .append(inventory, that.inventory)
                .append(viewer, that.viewer)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(interpreter)
                .append(title)
                .append(width)
                .append(height)
                .append(components)
                .append(inventory)
                .append(viewer)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("interpreter", interpreter)
                .append("title", title)
                .append("width", width)
                .append("height", height)
                .append("components", components)
                .append("inventory", inventory)
                .append("viewer", viewer)
                .toString();
    }

    @Override
    public @NotNull GuiContext getContext() {
        return this;
    }
}
