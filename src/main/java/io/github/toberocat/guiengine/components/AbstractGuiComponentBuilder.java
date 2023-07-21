package io.github.toberocat.guiengine.components;

import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An abstract base class for building GUI components.
 * This class provides common properties and methods for building GUI components.
 * Created: 10.07.2023
 * Author: Tobias Madlberger (Tobias)
 *
 * @param <B> The type of the builder class.
 */
public abstract class AbstractGuiComponentBuilder<B extends AbstractGuiComponentBuilder<B>> implements GuiComponentBuilder {

    protected @NotNull RenderPriority priority = RenderPriority.NORMAL;
    protected @NotNull String id = UUID.randomUUID().toString();
    protected int x = 0;
    protected int y = 0;
    protected boolean hidden = false;

    protected @NotNull List<GuiFunction> clickFunctions = new ArrayList<>();
    protected @NotNull List<GuiFunction> dragFunctions = new ArrayList<>();
    protected @NotNull List<GuiFunction> closeFunctions = new ArrayList<>();

    /**
     * Set the rendering priority of the GUI component.
     *
     * @param priority The RenderPriority to set.
     * @return The builder instance.
     */
    public @NotNull B setPriority(RenderPriority priority) {
        this.priority = priority;
        return self();
    }

    /**
     * Set the ID of the GUI component.
     *
     * @param id The ID to set.
     * @return The builder instance.
     */
    public @NotNull B setId(String id) {
        this.id = id;
        return self();
    }

    /**
     * Set the X coordinate of the GUI component.
     *
     * @param x The X coordinate to set.
     * @return The builder instance.
     */
    public @NotNull B setX(int x) {
        this.x = x;
        return self();
    }

    /**
     * Set the Y coordinate of the GUI component.
     *
     * @param y The Y coordinate to set.
     * @return The builder instance.
     */
    public @NotNull B setY(int y) {
        this.y = y;
        return self();
    }

    /**
     * Set the visibility of the GUI component.
     *
     * @param hidden true to hide the component, false to make it visible.
     * @return The builder instance.
     */
    public @NotNull B setHidden(boolean hidden) {
        this.hidden = hidden;
        return self();
    }

    /**
     * Set the click functions for the GUI component.
     *
     * @param clickFunctions The list of click functions to set.
     * @return The builder instance.
     */
    public @NotNull B setClickFunctions(List<GuiFunction> clickFunctions) {
        this.clickFunctions = clickFunctions;
        return self();
    }

    /**
     * Set the drag functions for the GUI component.
     *
     * @param dragFunctions The list of drag functions to set.
     * @return The builder instance.
     */
    public @NotNull B setDragFunctions(List<GuiFunction> dragFunctions) {
        this.dragFunctions = dragFunctions;
        return self();
    }

    /**
     * Set the close functions for the GUI component.
     *
     * @param closeFunctions The list of close functions to set.
     * @return The builder instance.
     */
    public @NotNull B setCloseFunctions(List<GuiFunction> closeFunctions) {
        this.closeFunctions = closeFunctions;
        return self();
    }

    /**
     * Get the ID of the GUI component.
     *
     * @return The ID of the GUI component.
     */
    public @NotNull String getId() {
        return id;
    }

    /**
     * Deserialize the properties of the GUI component from the provided ParserContext node.
     *
     * @param node The ParserContext node containing the properties of the GUI component.
     * @throws IOException If there is an error while deserializing the properties.
     */
    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        setPriority(node.getOptionalRenderPriority("priority").orElse(RenderPriority.NORMAL));
        setId(node.getOptionalString("id").orElse(id));
        setClickFunctions(node.getFunctions("on-click").orElse(new ArrayList<>()));
        setDragFunctions(node.getFunctions("on-drag").orElse(new ArrayList<>()));
        setCloseFunctions(node.getFunctions("on-close").orElse(new ArrayList<>()));
        setX(node.getOptionalInt("x").orElse(0));
        setY(node.getOptionalInt("y").orElse(0));
        setHidden(node.getOptionalBoolean("hidden").orElse(false));
    }

    /**
     * Helper method to return the builder instance.
     *
     * @return The builder instance.
     */
    @SuppressWarnings("unchecked")
    protected final B self() {
        return (B) this;
    }
}
