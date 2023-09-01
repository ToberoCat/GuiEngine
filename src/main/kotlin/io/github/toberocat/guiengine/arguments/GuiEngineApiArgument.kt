package io.github.toberocat.guiengine.arguments

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player

class GuiEngineApiArgument @JvmOverloads constructor(private val defaultApi: GuiEngineApi? = plugin.guiApi) :
    Argument<GuiEngineApi?> {
    @Throws(CommandException::class)
    override fun parse(player: Player, s: String): GuiEngineApi? {
        return GuiEngineApi.APIS[s]
            ?: throw CommandException(
                "§cNo API found with ID$s",
                HashMap()
            )
    }

    override fun defaultValue(player: Player): GuiEngineApi? {
        return defaultApi
    }

    @Throws(CommandException::class)
    override fun tab(player: Player): List<String>? {
        return GuiEngineApi.APIS.keys.stream().toList()
    }

    override fun descriptionKey(): String {
        return "base.gui-engine.args.api"
    }

    override fun usage(): String {
        return "<api>"
    }
}
