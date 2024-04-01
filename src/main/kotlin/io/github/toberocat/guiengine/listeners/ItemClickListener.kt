package io.github.toberocat.guiengine.listeners

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.commands.GiveCommand
import io.github.toberocat.guiengine.utils.getPersistentData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

/**
 * ItemClickListener is a Bukkit event listener responsible for handling player interactions with GUI items.
 *
 *
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class ItemClickListener : Listener {
    /**
     * Handles the PlayerInteractEvent when a player interacts with a GUI item.
     *
     * @param event The PlayerInteractEvent to handle.
     */
    @EventHandler
    private fun click(event: PlayerInteractEvent) {
        val stack = event.item ?: return
        val apiId = stack.getPersistentData(GiveCommand.API_NAME_KEY, PersistentDataType.STRING) ?: return
        val guiId = stack.getPersistentData(GiveCommand.GUI_ID_KEY, PersistentDataType.STRING) ?: return

        val api = GuiEngineApi.APIS[apiId] ?: return
        event.isCancelled = true
        api.openGui(event.player, guiId)
    }
}
