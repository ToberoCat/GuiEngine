package io.github.toberocat.guiengine.action

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.exception.GuiActionException
import io.github.toberocat.guiengine.utils.GuiEngineAction
import io.github.toberocat.guiengine.utils.validate
import org.bukkit.entity.Player
import java.util.logging.Level

/**
 * The `OpenGuiAction` class represents an action that allows a player to open a GUI using the `GuiEngineApi`.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 29.04.2023
 */
class OpenGuiAction : GuiEngineAction() {
    override fun label() = "open"

    override fun run(player: Player, args: Array<String>) {
        validate("Not all arguments present") { args.size != 2 }
        val apiId = args[0]
        val guiId = args[1]
        val api = GuiEngineApi.APIS[apiId] ?: throw GuiActionException(this, "Api '$apiId' not found")
        try {
            api.openGui(player, guiId)
        } catch (e: Exception) {
            plugin.logger.log(Level.WARNING, e.message)
            player.sendMessage("A error occurred. ${e.message}")
        }
    }
}
