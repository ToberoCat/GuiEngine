package io.github.toberocat.guiengine.listeners;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.commands.GiveCommand;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ItemClickListener implements Listener {

    @EventHandler
    private void click(@NotNull PlayerInteractEvent event) {
        ItemStack stack = event.getItem();
        if (stack == null)
            return;
        String apiId = ItemUtils.getPersistent(stack, GiveCommand.API_NAME_KEY, PersistentDataType.STRING);
        String guiId = ItemUtils.getPersistent(stack, GiveCommand.GUI_ID_KEY, PersistentDataType.STRING);
        if (apiId == null || guiId == null)
            return;
        GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
        if (api == null)
            return;

        event.setCancelled(true);
        api.openGui(event.getPlayer(), guiId);
    }
}
