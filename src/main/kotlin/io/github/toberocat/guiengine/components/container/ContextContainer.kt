package io.github.toberocat.guiengine.components.container

import io.github.toberocat.guiengine.context.GuiContext

interface ContextContainer {
    fun addContext(context: GuiContext)
    fun clearContainer()
}