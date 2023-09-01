package io.github.toberocat.guiengine.view

import org.bukkit.entity.Player
import java.util.*

/**
 * An interface representing a manager for GUI views in a Minecraft plugin.
 */
interface GuiViewManager {
    /**
     * Get the GUI view associated with the given player UUID.
     *
     * @param player The UUID of the player.
     * @return The GuiView associated with the player, or null if not found.
     */
    fun getGuiView(player: UUID): GuiView?

    /**
     * Check if the given player is currently viewing a GUI.
     *
     * @param player The UUID of the player.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    fun isViewingGui(player: UUID): Boolean

    /**
     * Register a new GUI view for the given player.
     *
     * @param player  The UUID of the player.
     * @param guiView The GuiView to be registered.
     */
    fun registerGui(player: UUID, guiView: GuiView)

    /**
     * Get the GUI view associated with the given Bukkit Player object.
     * This is a default method that delegates to the UUID-based version of getGuiView.
     *
     * @param player The Bukkit Player object.
     * @return The GuiView associated with the player, or null if not found.
     */
    fun getGuiView(player: Player): GuiView? {
        return getGuiView(player.uniqueId)
    }

    /**
     * Check if the given Bukkit Player object is currently viewing a GUI.
     * This is a default method that delegates to the UUID-based version of isViewingGui.
     *
     * @param player The Bukkit Player object.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    fun isViewingGui(player: Player): Boolean {
        return isViewingGui(player.uniqueId)
    }
}
