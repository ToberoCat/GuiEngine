package io.github.toberocat.guiengine.api.components.provided.embedded;

import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EmbeddedGuiComponentBuilder {
    private @NotNull String id = UUID.randomUUID().toString();
    private int x = 0;
    private int y = 0;
    private int width = 1;
    private int height = 1;
    private @NotNull RenderPriority priority = RenderPriority.NORMAL;
    private @Nullable String targetGui;
    private boolean copyAir = true;
    private boolean interactions = true;
    private boolean hidden = false;

    public @NotNull EmbeddedGuiComponentBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setPriority(@NotNull RenderPriority priority) {
        this.priority = priority;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setTargetGui(@NotNull String targetGui) {
        this.targetGui = targetGui;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setCopyAir(boolean copyAir) {
        this.copyAir = copyAir;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setInteractions(boolean interactions) {
        this.interactions = interactions;
        return this;
    }

    public @NotNull EmbeddedGuiComponentBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public @NotNull EmbeddedGuiComponent createEmbeddedGuiComponent() {
        assert targetGui != null;
        EmbeddedGuiComponent component = new EmbeddedGuiComponent(id,
                x,
                y,
                width,
                height,
                priority,
                targetGui,
                copyAir,
                interactions);
        component.setHidden(hidden);
        return component;
    }
}