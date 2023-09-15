package io.github.toberocat.guiengine.components.container

import io.github.toberocat.guiengine.components.GuiComponent

interface LayoutContainer : GuiComponent {
    fun addComponent(component: GuiComponent)
    fun clearContainer()
}