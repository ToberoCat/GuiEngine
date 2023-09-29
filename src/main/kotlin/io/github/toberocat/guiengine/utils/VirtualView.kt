package io.github.toberocat.guiengine.utils

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView

class VirtualView(private val player: Player?) : InventoryView() {
    override fun getTopInventory(): Inventory {
        TODO("Not yet implemented")
    }

    override fun getBottomInventory(): Inventory {
        TODO("Not yet implemented")
    }

    override fun getPlayer() = player ?: throw IllegalArgumentException("Virtualized view has no player")

    override fun getType() = InventoryType.CHEST

    override fun getTitle() = "Virtualized View"
}