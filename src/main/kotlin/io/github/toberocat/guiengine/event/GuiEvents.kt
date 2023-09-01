package io.github.toberocat.guiengine.event

import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

/**
 * Represents a set of defaulted GUI-related events.
 * These events can be overridden in classes that implement this interface to handle specific GUI interactions.
 * Events can be triggered when a player clicks on a component, drags an item in the GUI, or closes the GUI.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiEvents {
    /**
     * Called when a player clicks on a component in the GUI.
     * The default behavior is to cancel the event and deny its result.
     * If the action does not involve dropping an item, the event handling is skipped.
     *
     * @param event The `InventoryClickEvent` representing the click event.
     */
    fun clickedComponent(event: InventoryClickEvent) {
        event.isCancelled = true
        event.result = Event.Result.DENY
    }

    /**
     * Called when a player drags an item in the GUI.
     * The default behavior is to cancel the event.
     *
     * @param event The `InventoryDragEvent` representing the drag event.
     */
    fun draggedComponent(event: InventoryDragEvent) {
        event.isCancelled = true
    }

    /**
     * Called when a player closes the GUI.
     * The default behavior is to do nothing.
     *
     * @param event The `InventoryCloseEvent` representing the close event.
     */
    fun closedComponent(event: InventoryCloseEvent) {}

    /**
     * Called when the GUI is initialized and the viewer (player) opens it.
     * This method is intended to handle any necessary initialization tasks before the GUI is shown to the player.
     * The `placeholders` parameter can be used to store additional information specific to the GUI.
     * The default behavior is to do nothing.
     *
     * @param placeholders A map containing placeholder data for the GUI.
     */
    fun onViewInit(placeholders: Map<String, String>) {}
}
