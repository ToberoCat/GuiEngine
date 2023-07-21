package io.github.toberocat.guiengine.listeners;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    private void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        send(player);
    }

    public static void send(@NotNull Player player) {
        if (!player.isOp()) return;
        if (GuiEngineApiPlugin.LATEST_VERSION) return;

        player.sendMessage("§bA newer version of §eGuiEngine§b is available for you. Check it out on spigotmc.org");
    }
}
