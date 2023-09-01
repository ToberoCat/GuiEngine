package io.github.toberocat.guiengine.commands

import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.command.CommandSender

class WebEditorCommand : SubCommand("webeditor") {
    override fun options(): Options = Options()

    override fun arguments(): Array<Argument<*>> = emptyArray()

    @Throws(CommandException::class)
    override fun handleCommand(
        sender: CommandSender,
        strings: Array<String>
    ): Boolean {
        sender.sendMessage("§6The Gui web editor is only available for gui engine premium users")
        return true
    }
}
