package io.github.toberocat.guiengine.commands

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.exception.GuiIORuntimeException
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException
import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.entity.Player

/**
 * The `DumpCommand` class represents a player sub-command that allows a player to open a GUI and dump its context.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 14.07.2023
 */
class DumpCommand
/**
 * Constructs a new `DumpCommand`.
 */
    : OpenCommand("dump-open") {
    @Throws(CommandException::class)
    override fun handle(player: Player, args: Array<String>): Boolean {
        if (0 == args.size) throw CommandException("This command needs a GUI ID provided", HashMap())
        try {
            val apiId = args[0]
            val guiId = args[1]
            val api = GuiEngineApi.APIS[apiId]
            if (null == api) {
                player.sendMessage("§cNo API found with ID $apiId")
                return false
            }
            val context = api.openGui(player, guiId)
            plugin.logger.info(context.toString())
        } catch (e: GuiNotFoundRuntimeException) {
            throw CommandException(e.message, HashMap())
        } catch (e: GuiIORuntimeException) {
            throw CommandException(e.message, HashMap())
        }
        return true
    }
}
