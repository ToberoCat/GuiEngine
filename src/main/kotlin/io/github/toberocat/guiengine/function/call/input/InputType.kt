package io.github.toberocat.guiengine.function.call.input

import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.toberocore.input.ChatInput
import org.bukkit.entity.Player
import java.util.function.Consumer

enum class InputType {
    CHAT {
        override fun takeInput(player: Player, context: ParserContext, callback: Consumer<String>) {
            player.closeInventory()
            val message = context.string("message").require("Chat", javaClass)
            ChatInput.prompt(GuiEngineApiPlugin.plugin, player, message, callback)
        }
    };

    abstract fun takeInput(player: Player, context: ParserContext, callback: Consumer<String>)
}