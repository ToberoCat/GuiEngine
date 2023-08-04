package io.github.toberocat.guiengine.listeners;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.commands.GiveCommand;
import io.github.toberocat.toberocore.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * ItemClickListener is a Bukkit event listener responsible for handling player interactions with GUI items.
 * <p>
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class ItemClickListener implements Listener {

    /**
     * Handles the PlayerInteractEvent when a player interacts with a GUI item.
     *
     * @param event The PlayerInteractEvent to handle.
     */
    @EventHandler
    private void click(@NotNull PlayerInteractEvent event) {
        ItemStack stack = event.getItem();
        if (null == stack) return;

        String apiId = ItemUtils.getPersistent(stack, GiveCommand.API_NAME_KEY, PersistentDataType.STRING);
        String guiId = ItemUtils.getPersistent(stack, GiveCommand.GUI_ID_KEY, PersistentDataType.STRING);

        if (null == apiId || null == guiId) return;

        GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
        if (null == api) return;

        event.setCancelled(true);
        api.openGui(event.getPlayer(), guiId);
    }
}
