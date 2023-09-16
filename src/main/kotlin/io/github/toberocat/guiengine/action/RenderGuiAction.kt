package io.github.toberocat.guiengine.action

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.toberocore.action.Action
import org.bukkit.entity.Player

class RenderGuiAction(val context: GuiContext) : Action() {
    override fun label() = "rerender"

    override fun run(player: Player) = context.render()
}