package io.github.toberocat.guiengine.item;

import io.github.toberocat.toberocore.item.ItemCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * GuiItemManager is responsible for managing and providing access to known GUI items.
 * <p>
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiItemManager {

    /**
     * A map that associates names of GUI items with their corresponding ItemStack representations.
     */
    private final @NotNull Map<String, ItemStack> knownItems = new HashMap<>();

    /**
     * Creates a new GuiItemManager and initializes it with GUI items loaded from the given FileConfiguration.
     *
     * @param config The FileConfiguration containing the configuration data for GUI items.
     */
    public GuiItemManager(@NotNull FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("items");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            knownItems.put(key, ItemCore.create(itemSection));
        }
    }

    /**
     * Retrieves the ItemStack representation of the GUI item with the given name.
     *
     * @param name The name of the GUI item to retrieve.
     * @return The ItemStack representation of the GUI item if found, or null if not present in the manager.
     */
    public @Nullable ItemStack getItem(@NotNull String name) {
        return knownItems.get(name);
    }

    /**
     * Returns a map containing all the known GUI items, where keys are the names of the items,
     * and values are their corresponding ItemStack representations.
     *
     * @return A map of known GUI items with names as keys and ItemStacks as values.
     */
    public @NotNull Map<String, ItemStack> getKnownItems() {
        return knownItems;
    }
}
