package io.github.toberocat.guiengine.utils.logger

import org.bukkit.command.CommandSender

class CommandSenderLogger(private val sender: CommandSender, private val debug: Boolean = false) : GuiLogger {
    override fun info(message: String) = sender.sendMessage("§8[§eGuiEngine§8] §7$message")

    override fun debug(message: String) {
        if (debug)
            sender.sendMessage("§8[§eGuiEngine§8][DEBUG] §7$message")
    }

    override fun error(message: String?) = sender.sendMessage("§8[§eGuiEngine§8] §cError: $message")
}