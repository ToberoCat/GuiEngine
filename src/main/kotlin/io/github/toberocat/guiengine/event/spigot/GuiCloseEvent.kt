package io.github.toberocat.guiengine.event.spigot

import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Represents a custom GUI close event triggered when a player closes a GUI.
 * It extends the `GuiEngineEvent` class and provides additional information about the close event.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiCloseEvent(context: GuiContext, val closeEvent: InventoryCloseEvent) : GuiEngineEvent(context) {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
}