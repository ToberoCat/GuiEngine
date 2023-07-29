package io.github.toberocat.guiengine.listeners;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerJoinListener is a Bukkit event listener that notifies players with operator (OP) status about
 * newer versions of GuiEngine available on SpigotMC.
 * <p>
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class PlayerJoinListener implements Listener {

    /**
     * Handles the PlayerJoinEvent when a player joins the server.
     *
     * @param event The PlayerJoinEvent to handle.
     */
    @EventHandler
    private void join(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        send(player);
    }

    /**
     * Notifies the player with the latest version information if they are an operator (OP) and
     * there is a newer version of GuiEngine available on SpigotMC.
     *
     * @param player The player to notify.
     */
    public static void send(@NotNull Player player) {
        if (!player.isOp()) return;
        if (GuiEngineApiPlugin.LATEST_VERSION) return;

        player.sendMessage("§bA newer version of §eGuiEngine§b is available for you. Check it out on spigotmc.org");
    }
}
