package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public abstract class AbstractGuiComponent implements GuiComponent {
    protected int offsetX, offsetY;
    protected final int width, height;
    protected @Nullable GuiContext context;
    protected @Nullable GuiEngineApi api;
    private final @NotNull RenderPriority priority;
    private final @NotNull String id;
    protected final @NotNull List<GuiFunction> clickFunctions;
    protected final @NotNull List<GuiFunction> dragFunctions;
    protected final @NotNull List<GuiFunction> closeFunctions;

    private boolean hidden;

    public AbstractGuiComponent(int offsetX,
                                int offsetY,
                                int width,
                                int height,
                                @NotNull RenderPriority priority,
                                @NotNull String id,
                                @NotNull List<GuiFunction> clickFunctions,
                                @NotNull List<GuiFunction> dragFunctions,
                                @NotNull List<GuiFunction> closeFunctions,
                                boolean hidden) {
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
    public void serialize(@NotNull JsonGenerator gen,
                          @NotNull SerializerProvider serializers) throws IOException {
        gen.writeStringField("type", getType());
        gen.writeStringField("id", getId());
        gen.writeStringField("priority", renderPriority().toString());
        gen.writeNumberField("x", offsetX());
        gen.writeNumberField("y", offsetY());
        gen.writeNumberField("width", width());
        gen.writeNumberField("height", height());
        gen.writeBooleanField("hidden", hidden());

        gen.writePOJOField("on-click", clickFunctions);
        gen.writePOJOField("on-drag", dragFunctions);
        gen.writePOJOField("on-close", closeFunctions);
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        if (context == null || api == null)
            return;

        FunctionProcessor.callFunctions(clickFunctions, api, context);
        context.render();
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
        if (context == null || api == null)
            return;

        FunctionProcessor.callFunctions(dragFunctions, api, context);
        context.render();
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        if (context == null || api == null)
            return;

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
        this.offsetX = x;
    }

    @Override
    public void setOffsetY(int y) {
        this.offsetY = y;
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

    public @NotNull List<GuiFunction> getClickFunctions() {
        return clickFunctions;
    }

    public @NotNull List<GuiFunction> getDragFunctions() {
        return dragFunctions;
    }

    public @NotNull List<GuiFunction> getCloseFunctions() {
        return closeFunctions;
    }
}
