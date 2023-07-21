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
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiItemManager {
    private final @NotNull Map<String, ItemStack> knownItems = new HashMap<>();

    public GuiItemManager(@NotNull FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("items");
        if (section == null)
            return;
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null)
                continue;

            knownItems.put(key, ItemCore.create(itemSection));
        }
    }

    public @Nullable ItemStack getItem(@NotNull String name) {
        return knownItems.get(name);
    }

    public @NotNull Map<String, ItemStack> getKnownItems() {
        return knownItems;
    }
}
