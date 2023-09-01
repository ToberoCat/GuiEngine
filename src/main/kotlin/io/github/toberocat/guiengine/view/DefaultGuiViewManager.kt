package io.github.toberocat.guiengine.view

import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import java.util.*

/**
 * An implementation of the GuiViewManager interface for managing GUI views in a Minecraft plugin.
 * This class keeps track of the opened inventories and handles various inventory-related events.
 */
class DefaultGuiViewManager : Listener, GuiViewManager {
    /**
     * A map to store the opened inventories, mapped to the UUID of the player.
     */
    private val openedInventories: MutableMap<UUID, GuiView>

    /**
     * Create a new instance of DefaultGuiViewManager.
     * It initializes the map to store opened inventories.
     */
    init {
        openedInventories = HashMap()
    }

    /**
     * Check if the given player is currently viewing a GUI.
     *
     * @param player The UUID of the player.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    override fun isViewingGui(player: UUID): Boolean {
        return openedInventories.containsKey(player)
    }

    /**
     * Register a new GUI view for the given player.
     *
     * @param player  The UUID of the player.
     * @param guiView The GuiView to be registered.
     */
    override fun registerGui(player: UUID, guiView: GuiView) {
        openedInventories[player] = guiView
    }

    /**
     * Get the GUI view associated with the given player UUID.
     *
     * @param player The UUID of the player.
     * @return The GuiView associated with the player, or null if not found.
     */
    override fun getGuiView(player: UUID): GuiView? {
        return openedInventories[player]
    }

    /**
     * Event handler for the InventoryClickEvent.
     * This method finds the corresponding GuiView associated with the clicked inventory
     * and calls the clickedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private fun click(event: InventoryClickEvent) {
        getView(event.whoClicked, event.inventory).ifPresent { view: GuiView -> view.context.clickedComponent(event) }
    }

    /**
     * Event handler for the InventoryDragEvent.
     * This method finds the corresponding GuiView associated with the dragged inventory
     * and calls the draggedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private fun drag(event: InventoryDragEvent) {
        getView(event.whoClicked, event.inventory).ifPresent { view: GuiView -> view.context.draggedComponent(event) }
    }

    /**
     * Event handler for the InventoryCloseEvent.
     * This method finds the corresponding GuiView associated with the closed inventory
     * and calls the closedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private fun close(event: InventoryCloseEvent) {
        getView(event.player, event.inventory).ifPresent { view: GuiView -> view.context.closedComponent(event) }
    }

    /**
     * Helper method to find the corresponding GuiView for a specific player and inventory.
     * It checks if the player is viewing a GUI, then searches for the GuiView associated with the given inventory.
     *
     * @param clicker   The HumanEntity that clicked/dragged/closed the inventory.
     * @param inventory The clicked/dragged/closed inventory.
     * @return An Optional containing the GuiView if found, or an empty Optional if not found.
     */
    private fun getView(clicker: HumanEntity, inventory: Inventory): Optional<GuiView> {
        return if (!isViewingGui(clicker.uniqueId)) Optional.empty() else openedInventories.values.stream()
            .filter { x: GuiView -> x.inventory == inventory }
            .findAny()
    }
}
