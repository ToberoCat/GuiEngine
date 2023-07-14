package io.github.toberocat.guiengine.components.provided.embedded;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        deserialize(node, true);
    }

    protected void deserialize(@NotNull ParserContext node, boolean forceTarget) throws IOException {
        super.deserialize(node);

        setCopyAir(node.getOptionalBoolean("copy-air").orElse(true));
        setInteractions(node.getOptionalBoolean("interactions").orElse(true));

        setWidth(node.getOptionalInt("width").orElseThrow(() ->
                new MissingRequiredParamException(this, "width")));
        setHeight(node.getOptionalInt("height").orElseThrow(() ->
                new MissingRequiredParamException(this, "height")));

        if (!forceTarget) setTargetGui(node.getOptionalString("target-gui").orElse(""));
        else setTargetGui(node.getOptionalString("target-gui").orElseThrow(() ->
                new MissingRequiredParamException(this, "target-gui")));

    }
}