package io.github.toberocat.guiengine.event.spigot

import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * Represents a custom GUI component drag event triggered when a player drags items in the GUI.
 * It extends the `GuiEngineEvent`
 * class and provides additional information about the drag event and the target component.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiComponentDragEvent(
    context: GuiContext,
    val dragEvent: InventoryDragEvent,
    val targetComponent: GuiComponent?
) : GuiEngineEvent(context)
