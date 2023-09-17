package io.github.toberocat.guiengine.commands

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.ArgLengthOption
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * The `OpenCommand` class represents a player sub-command that allows a player to open a GUI using the `GuiEngineApi`.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 07.04.2023
 */
open class OpenCommand
/**
 * Constructs a new `OpenCommand` with the specified label.
 *
 * @param label The label for this command.
 */
/**
 * Constructs a new `OpenCommand` with the default label "open".
 */
@JvmOverloads constructor(label: String = "open") : PlayerSubCommand(label) {
    override fun options(): Options = Options()
        .cmdOpt(ArgLengthOption(2))

    override fun arguments(): Array<Argument<*>> = emptyArray()

    @Throws(CommandException::class)
    override fun handle(player: Player, args: Array<String>): Boolean {
        if (args.isEmpty())
            throw CommandException("This command needs a GUI ID provided", HashMap())

        try {
            val apiId = args[0]
            val guiId = args[1]
            val api = GuiEngineApi.APIS[apiId]
            if (null == api) {
                player.sendMessage("§cNo API found with ID $apiId")
                return false
            }
            api.openGui(player, guiId)
        } catch (e: Exception) {
            throw CommandException(e.message, HashMap())
        }
        return true
    }

    @Throws(CommandException::class)
    override fun routeTab(sender: CommandSender, args: Array<String>): List<String>? {
        if (!sender.hasPermission(permission)) {
            return null
        }
        val newArgs = arrayOfNulls<String>(args.size - 1)
        System.arraycopy(args, 1, newArgs, 0, newArgs.size)
        if (1 >= newArgs.size) return GuiEngineApi.APIS.keys.stream().toList()
        val api = GuiEngineApi.APIS[newArgs[0]]
        if (null == api) {
            sender.sendMessage("§cNo API found with ID " + newArgs[0])
            return emptyList()
        }
        return api.getAvailableGuis().stream().toList()
    }
}
