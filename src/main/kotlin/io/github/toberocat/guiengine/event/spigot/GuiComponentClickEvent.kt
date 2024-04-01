package io.github.toberocat.guiengine.event.spigot

import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Represents a custom GUI component click event triggered when a player clicks on a GUI component.
 * It extends the `GuiEngineEvent`
 * class and provides additional information about the click event and the target component.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiComponentClickEvent(
    context: GuiContext, val clickEvent: InventoryClickEvent, val targetComponent: GuiComponent?
) : GuiEngineEvent(context) {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        val handlerList = HandlerList()
    }
}
