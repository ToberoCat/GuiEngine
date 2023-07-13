package io.github.toberocat.guiengine.utils;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public final class Utils {
    public static int translateToSlot(int x, int y) {
        return y * 9 + x;
    }

    public static @NotNull CoordinatePair translateFromSlot(int slot) {
        return new CoordinatePair(slot % 9, slot / 9);
    }

    public static @NotNull ItemStack processItemStack(@NotNull ItemStack itemStack,
                                                      @NotNull GuiEngineApi api,
                                                      @NotNull GuiContext context) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return itemStack;

        meta.setDisplayName(FunctionProcessor.applyFunctions(api, context, meta.getDisplayName()));
        List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        meta.setLore(lore.stream()
                .map(x -> FunctionProcessor.applyFunctions(api, context, x))
                .toList());
        ItemStack copy = itemStack.clone();
        copy.setItemMeta(meta);
        return copy;
    }
}
