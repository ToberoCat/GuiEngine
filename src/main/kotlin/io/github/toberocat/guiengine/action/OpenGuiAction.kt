package io.github.toberocat.guiengine.action

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.utils.GuiEngineAction
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
    /**
     * Returns the label of this action, which is "open".
     *
     * @return The label of this action.
     */
    override fun label(): String {
        return "open"
    }

    /**
     * Performs the action of opening a GUI for the specified player using the `GuiEngineApi`.
     *
     * @param player The player who triggered the action.
     * @param args   The arguments passed to the action. The first argument is expected to be the API ID, and the second argument is the GUI ID.
     */
    override fun run(player: Player, args: Array<String>) {
        if (2 != args.size) return
        val apiId = args[0]
        val guiId = args[1]
        val api = GuiEngineApi.APIS[apiId] ?: return
        try {
            api.openGui(player, guiId)
        } catch (e: Exception) {
            plugin.logger.log(Level.WARNING, e.message)
        }
    }
}
