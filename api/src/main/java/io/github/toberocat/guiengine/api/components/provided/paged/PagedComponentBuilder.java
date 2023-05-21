package io.github.toberocat.guiengine.api.components.provided.paged;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PagedComponentBuilder {
    private @NotNull String id = UUID.randomUUID().toString();
    private int offsetX = 0;
    private int offsetY = 0;
    private int width = 1;
    private int height = 1;
    private int showingPage = 0;
    private int[] pattern = new int[0];
    private @NotNull RenderPriority priority = RenderPriority.NORMAL;
    private @Nullable JsonNode parent;
    private boolean hidden = false;
    private boolean copyAir = true;
    private boolean interactions = true;

    public @NotNull PagedComponentBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public @NotNull PagedComponentBuilder setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public @NotNull PagedComponentBuilder setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public @NotNull PagedComponentBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public @NotNull PagedComponentBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public @NotNull PagedComponentBuilder setPattern(int[] pattern) {
        this.pattern = pattern;
        return this;
    }

    public @NotNull PagedComponentBuilder setPriority(@NotNull RenderPriority priority) {
        this.priority = priority;
        return this;
    }

    public @NotNull PagedComponentBuilder setParent(@NotNull JsonNode parent) {
        this.parent = parent;
        return this;
    }

    public @NotNull PagedComponentBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public @NotNull PagedComponentBuilder setShowingPage(int showingPage) {
        this.showingPage = showingPage;
        return this;
    }

    public @NotNull PagedComponentBuilder setCopyAir(boolean copyAir) {
        this.copyAir = copyAir;
        return this;
    }

    public @NotNull PagedComponentBuilder setInteractions(boolean interactions) {
        this.interactions = interactions;
        return this;
    }

    public @NotNull PagedComponent createPagedComponent() {
        assert parent != null;
        PagedComponent component = new PagedComponent(id, offsetX, offsetY, width, height,
                pattern, priority, parent, showingPage, copyAir, interactions);
        component.setHidden(hidden);
        return component;
    }
}