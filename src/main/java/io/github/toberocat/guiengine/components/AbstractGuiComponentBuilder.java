package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.toberocat.guiengine.utils.JsonUtils.*;

/**
 * Created: 10.07.2023
 *
 * @author Tobias Madlberger (Tobias)
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

    public @NotNull B setPriority(RenderPriority priority) {
        this.priority = priority;
        return self();
    }

    public @NotNull B setId(String id) {
        this.id = id;
        return self();
    }

    public @NotNull B setX(int x) {
        this.x = x;
        return self();
    }

    public @NotNull B setY(int y) {
        this.y = y;
        return self();
    }

    public @NotNull B setHidden(boolean hidden) {
        this.hidden = hidden;
        return self();
    }

    public @NotNull B setClickFunctions(List<GuiFunction> clickFunctions) {
        this.clickFunctions = clickFunctions;
        return self();
    }

    public @NotNull B setDragFunctions(List<GuiFunction> dragFunctions) {
        this.dragFunctions = dragFunctions;
        return self();
    }

    public @NotNull B setCloseFunctions(List<GuiFunction> closeFunctions) {
        this.closeFunctions = closeFunctions;
        return self();
    }

    public @NotNull String getId() {
        return id;
    }

    @Override
    public void deserialize(@NotNull JsonNode node) throws IOException {
        setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL));
        setId(getOptionalString(node, "id").orElseThrow());
        setClickFunctions(getFunctions(node, "on-click").orElse(new ArrayList<>()));
        setDragFunctions(getFunctions(node, "on-drag").orElse(new ArrayList<>()));
        setCloseFunctions(getFunctions(node, "on-close").orElse(new ArrayList<>()));
        setX(getOptionalInt(node, "x").orElse(0));
        setY(getOptionalInt(node, "y").orElse(0));
        setHidden(getOptionalBoolean(node, "hidden").orElse(false));
    }

    @SuppressWarnings("unchecked")
    protected final B self() {
        return (B) this;
    }
}
