package io.github.toberocat.guiengine.api.components.provided.embedded;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.api.exception.InvalidGuiComponentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;
import static io.github.toberocat.guiengine.api.utils.JsonUtils.getOptionalBoolean;

public class EmbeddedGuiComponentBuilder<B extends EmbeddedGuiComponentBuilder<B>> extends AbstractGuiComponentBuilder<B> {
    protected int width = 1;
    protected int height = 1;
    protected @Nullable String targetGui;
    protected boolean copyAir = true;
    protected boolean interactions = true;

    public @NotNull B setWidth(int width) {
        this.width = width;
        return self();
    }

    public @NotNull B setHeight(int height) {
        this.height = height;
        return self();
    }


    public @NotNull B setTargetGui(@NotNull String targetGui) {
        this.targetGui = targetGui;
        return self();
    }

    public @NotNull B setCopyAir(boolean copyAir) {
        this.copyAir = copyAir;
        return self();
    }

    public @NotNull B setInteractions(boolean interactions) {
        this.interactions = interactions;
        return self();
    }

    @Override
    public @NotNull EmbeddedGuiComponent createComponent() {
        assert targetGui != null;
        return new EmbeddedGuiComponent(x, y, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden, targetGui, copyAir, interactions);
    }

    public static class Factory<B extends EmbeddedGuiComponentBuilder<B>> extends AbstractGuiComponentBuilder.Factory<B> {

        @Override
        public @NotNull B createBuilder() {
            return (B) new EmbeddedGuiComponentBuilder<B>();
        }

        @Override
        public void deserialize(@NotNull JsonNode node, @NotNull B builder) throws IOException {
            super.deserialize(node, builder);
            builder.setWidth(getOptionalInt(node, "width").orElseThrow(() ->
                            new InvalidGuiComponentException("The component is missing a required argument 'width'")))
                    .setHeight(getOptionalInt(node, "height").orElseThrow(() ->
                            new InvalidGuiComponentException("The component is missing a required argument 'height'")))
                    .setTargetGui(getOptionalString(node, "target-gui").orElseThrow(() ->
                            new InvalidGuiComponentException("The component is missing a required argument 'target-gui'")))
                    .setCopyAir(getOptionalBoolean(node, "copy-air").orElse(true))
                    .setInteractions(getOptionalBoolean(node, "interactions").orElse(true));
        }
    }
}