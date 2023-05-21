package io.github.toberocat.guiengine.api.components.provided.toggle;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ToggleItemComponentBuilder {
    private @NotNull RenderPriority priority = RenderPriority.NORMAL;
    private @NotNull String id = UUID.randomUUID().toString();
    private @Nullable JsonNode options;
    private int selected;
    private int x;
    private int y;
    private boolean hidden;

    public @NotNull ToggleItemComponentBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setPriority(@NotNull RenderPriority priority) {
        this.priority = priority;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setOptions(@NotNull JsonNode options) {
        this.options = options;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setSelected(int selected) {
        this.selected = selected;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public @NotNull ToggleItemComponent createToggleItemComponent() {
        assert options != null;
        ToggleItemComponent component = new ToggleItemComponent(priority, id, options, selected, x, y);
        component.setHidden(hidden);
        return component;
    }
}