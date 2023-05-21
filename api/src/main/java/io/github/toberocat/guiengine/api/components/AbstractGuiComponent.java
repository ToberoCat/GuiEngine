package io.github.toberocat.guiengine.api.components;

import com.fasterxml.jackson.core.JsonGenerator;
import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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

    private boolean hidden;

    public AbstractGuiComponent(@NotNull String id,
                                int offsetX,
                                int offsetY,
                                int width,
                                int height,
                                @NotNull RenderPriority priority) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.priority = priority;
        this.id = id;
    }

    public void addParentValues(@NotNull JsonGenerator gen) throws IOException {
        gen.writeStringField("type", getType());
        gen.writeStringField("id", getId());
        gen.writeStringField("priority", renderPriority().toString());
        gen.writeNumberField("x", offsetX());
        gen.writeNumberField("y", offsetY());
        gen.writeNumberField("width", width());
        gen.writeNumberField("height", height());
        gen.writeBooleanField("hidden", hidden());
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
}
