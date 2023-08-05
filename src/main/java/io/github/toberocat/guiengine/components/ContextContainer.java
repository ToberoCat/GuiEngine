package io.github.toberocat.guiengine.components;

import io.github.toberocat.guiengine.context.GuiContext;

public interface ContextContainer {
    void addContext(GuiContext context);

    void clearContainer();
}
