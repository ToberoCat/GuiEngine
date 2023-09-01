package io.github.toberocat.guiengine.item

import io.github.toberocat.toberocore.item.ItemCore
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack

/**
 * GuiItemManager is responsible for managing and providing access to known GUI items.
 *
 *
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiItemManager(config: FileConfiguration) {
    /**
     * A map that associates names of GUI items with their corresponding ItemStack representations.
     */
    private val knownItems: MutableMap<String, ItemStack> = HashMap()

    init {
        val section = config.getConfigurationSection("items")
        for (key in section?.getKeys(false) ?: emptySet()) {
            val itemSection = section?.getConfigurationSection(key) ?: continue
            knownItems[key] = ItemCore.create(itemSection)
        }
    }

    /**
     * Retrieves the ItemStack representation of the GUI item with the given name.
     *
     * @param name The name of the GUI item to retrieve.
     * @return The ItemStack representation of the GUI item if found, or null if not present in the manager.
     */
    operator fun get(name: String): ItemStack? = knownItems[name]

    /**
     * Returns a map containing all the known GUI items, where keys are the names of the items,
     * and values are their corresponding ItemStack representations.
     *
     * @return A map of known GUI items with names as keys and ItemStacks as values.
     */
    fun getKnownItems(): Map<String, ItemStack> = knownItems
}
