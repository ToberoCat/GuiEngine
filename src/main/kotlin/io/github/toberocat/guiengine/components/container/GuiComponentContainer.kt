package io.github.toberocat.guiengine.components.container

import io.github.toberocat.guiengine.components.GuiComponent

interface GuiComponentContainer {
    fun addComponent(component: GuiComponent)
    fun clearContainer()
}