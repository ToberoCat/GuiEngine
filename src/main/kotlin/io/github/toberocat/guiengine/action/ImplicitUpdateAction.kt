package io.github.toberocat.guiengine.action

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.utils.GuiEngineAction
import org.bukkit.entity.Player

class ImplicitUpdateAction(private val context: GuiContext) : GuiEngineAction() {
    override fun label() = "trigger-implicit-update"

    override fun run(player: Player) {
        context.computableFunctionProcessor.editAll(context)
    }
}