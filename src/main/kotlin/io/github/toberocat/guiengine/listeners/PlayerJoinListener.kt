package io.github.toberocat.guiengine.listeners

import io.github.toberocat.guiengine.GuiEngineApiPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * PlayerJoinListener is a Bukkit event listener that notifies players with operator (OP) status about
 * newer versions of GuiEngine available on SpigotMC.
 *
 *
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class PlayerJoinListener : Listener {
    /**
     * Handles the PlayerJoinEvent when a player joins the server.
     *
     * @param event The PlayerJoinEvent to handle.
     */
    @EventHandler
    private fun join(event: PlayerJoinEvent) = send(event.player)

    companion object {
        /**
         * Notifies the player with the latest version information if they are an operator (OP) and
         * there is a newer version of GuiEngine available on SpigotMC.
         *
         * @param player The player to notify.
         */
        fun send(player: Player) {
            if (!player.isOp) return
            if (GuiEngineApiPlugin.LATEST_VERSION) return
            player.sendMessage("§bA newer version of §eGuiEngine§b is available for you. Check it out on spigotmc.org")
        }
    }
}
