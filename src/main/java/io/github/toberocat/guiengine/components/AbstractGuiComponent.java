package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * An abstract base class for GUI components.
 * This class provides common functionality for GUI components and implements the GuiComponent interface.
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public abstract class AbstractGuiComponent implements GuiComponent {
    /**
     * X position of the component
     */
    protected int offsetX;
    /**
     * Y position of the component
     */
    protected int offsetY;
    /**
     * The width of this component
     */
    protected final int width;
    /**
     * The height of this component
     */
    protected final int height;
    /**
     * The context this component owns to
     */
    protected @Nullable GuiContext context;
    /**
     * The api this component belongs to
     */
    protected @Nullable GuiEngineApi api;

    /**
     * The render priority of this component. Determines the order of rendering nad the order of event receipation
     */
    protected final @NotNull RenderPriority priority;
    protected final @NotNull String id;

    /**
     * The functions that will get called once a component has been clicked
     */
    protected final @NotNull List<GuiFunction> clickFunctions;

    /**
     * The functions that will get called once a component is part of a drag event
     */
    protected final @NotNull List<GuiFunction> dragFunctions;

    /**
     * The functions that will get called when a component's gui gets closed
     */
    protected final @NotNull List<GuiFunction> closeFunctions;

    private boolean hidden;

    /**
     * Constructor for AbstractGuiComponent.
     *
     * @param offsetX        The X offset of the GUI component.
     * @param offsetY        The Y offset of the GUI component.
     * @param width          The width of the GUI component.
     * @param height         The height of the GUI component.
     * @param priority       The rendering priority of the GUI component.
     * @param id             The ID of the GUI component.
     * @param clickFunctions The list of click functions for the GUI component.
     * @param dragFunctions  The list of drag functions for the GUI component.
     * @param closeFunctions The list of close functions for the GUI component.
     * @param hidden         true if the GUI component is hidden, false otherwise.
     */
    protected AbstractGuiComponent(int offsetX, int offsetY, int width, int height, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, boolean hidden) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.priority = priority;
        this.id = id;
        this.clickFunctions = clickFunctions;
        this.dragFunctions = dragFunctions;
        this.closeFunctions = closeFunctions;
        this.hidden = hidden;
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        gen.writeStringField("type", getType());
        gen.writeStringField("id", id);
        gen.writeStringField("priority", renderPriority().toString());
        gen.writeNumberField("x", offsetX());
        gen.writeNumberField("y", offsetY());
        gen.writeNumberField("width", width());
        gen.writeNumberField("height", height());
        gen.writeBooleanField("hidden", hidden());

        gen.writeFunctionField("on-click", clickFunctions);
        gen.writeFunctionField("on-drag", dragFunctions);
        gen.writeFunctionField("on-close", closeFunctions);
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        if (null == context || null == api) return;

        FunctionProcessor.callFunctions(clickFunctions, api, context);
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
        if (null == context || null == api) return;

        FunctionProcessor.callFunctions(dragFunctions, api, context);
        context.render();
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        if (null == context || null == api) return;

        FunctionProcessor.callFunctions(closeFunctions, api, context);
        context.render();
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public @NotNull RenderPriority renderPriority() {
        return priority;
    }

    @Override
    public int offsetX() {
        return offsetX;
    }

    @Override
    public int offsetY() {
        return offsetY;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public boolean hidden() {
        return hidden;
    }

    @Override
    public void setOffsetX(int x) {
        offsetX = x;
    }

    @Override
    public void setOffsetY(int y) {
        offsetY = y;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public void setApi(@NotNull GuiEngineApi api) {
        this.api = api;
    }

    @Override
    public void setContext(@NotNull GuiContext context) {
        this.context = context;
    }

    /**
     * Get the list of click functions for the GUI component.
     *
     * @return The list of click functions.
     */
    public @NotNull List<GuiFunction> getClickFunctions() {
        return clickFunctions;
    }

    /**
     * Get the list of drag functions for the GUI component.
     *
     * @return The list of drag functions.
     */
    public @NotNull List<GuiFunction> getDragFunctions() {
        return dragFunctions;
    }

    /**
     * Get the list of close functions for the GUI component.
     *
     * @return The list of close functions.
     */
    public @NotNull List<GuiFunction> getCloseFunctions() {
        return closeFunctions;
    }
}
