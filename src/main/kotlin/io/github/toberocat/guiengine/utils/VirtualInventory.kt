package io.github.toberocat.guiengine.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class VirtualInventory(private val height: Int, private val reload: Runnable) : Inventory {
    override fun getSize(): Int {
        return height * 9
    }

    override fun getMaxStackSize(): Int {
        return 64
    }

    override fun setMaxStackSize(size: Int) {}
    override fun getItem(index: Int): ItemStack? {
        return null
    }

    override fun setItem(index: Int, item: ItemStack?) {}

    @Throws(IllegalArgumentException::class)
    override fun addItem(vararg items: ItemStack): HashMap<Int, ItemStack> {
        return HashMap()
    }

    @Throws(IllegalArgumentException::class)
    override fun removeItem(vararg items: ItemStack): HashMap<Int, ItemStack> {
        return HashMap()
    }

    override fun getContents(): Array<ItemStack> {
        return Array(54) { ItemStack(Material.AIR) }
    }

    @Throws(IllegalArgumentException::class)
    override fun setContents(items: Array<ItemStack>) {
        reload.run() // ToDo: Maybe make this one only reload own inventory
    }

    override fun getStorageContents(): Array<ItemStack> {
        return Array(0) { ItemStack(Material.AIR) }
    }

    @Throws(IllegalArgumentException::class)
    override fun setStorageContents(items: Array<ItemStack>) {
    }

    @Throws(IllegalArgumentException::class)
    override fun contains(material: Material): Boolean {
        return false
    }

    override fun contains(item: ItemStack?): Boolean {
        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun contains(material: Material, amount: Int): Boolean {
        return false
    }

    override fun contains(item: ItemStack?, amount: Int): Boolean {
        return false
    }

    override fun containsAtLeast(item: ItemStack?, amount: Int): Boolean {
        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun all(material: Material): HashMap<Int, out ItemStack> {
        return HashMap()
    }

    override fun all(item: ItemStack?): HashMap<Int, out ItemStack> {
        return HashMap()
    }

    @Throws(IllegalArgumentException::class)
    override fun first(material: Material): Int {
        return 0
    }

    override fun first(item: ItemStack): Int {
        return 0
    }

    override fun firstEmpty(): Int {
        return 0
    }

    override fun isEmpty(): Boolean {
        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun remove(material: Material) {
    }

    override fun remove(item: ItemStack) {}
    override fun clear(index: Int) {}
    override fun clear() {}
    override fun getViewers(): List<HumanEntity> {
        return ArrayList()
    }

    override fun getType(): InventoryType {
        return InventoryType.CHEST
    }

    override fun getHolder(): InventoryHolder? {
        return null
    }

    override fun iterator(): MutableListIterator<ItemStack> {
        return ArrayList<ItemStack>().listIterator()
    }

    override fun iterator(index: Int): ListIterator<ItemStack> {
        return ArrayList<ItemStack>().listIterator()
    }

    override fun getLocation(): Location? {
        return null
    }
}
