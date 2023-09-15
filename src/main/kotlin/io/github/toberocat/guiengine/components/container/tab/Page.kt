package io.github.toberocat.guiengine.components.container.tab

import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.context.GuiContext

interface Page {
    val pageContext: GuiContext
    fun insert(component: GuiComponent): Boolean
}