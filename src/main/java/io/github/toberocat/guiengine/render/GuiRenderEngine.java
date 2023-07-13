package io.github.toberocat.guiengine.render;

import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.view.GuiView;
import io.github.toberocat.guiengine.view.GuiViewManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created: 06.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiRenderEngine {
    @NotNull GuiViewManager getGuiViewManager();

    void renderGui(@NotNull ItemStack[][] renderBuffer,
                   @NotNull GuiContext context,
                   @NotNull Player viewer);

    default void showGui(@NotNull GuiContext content,
                         @NotNull Player viewer,
                         @NotNull Map<String, String> placeholders) {
        Inventory inventory = createInventory(content, viewer, placeholders);

        getGuiViewManager().registerGui(viewer.getUniqueId(), new GuiView(inventory, content));
        viewer.openInventory(inventory);
    }

    default @NotNull Inventory createInventory(@NotNull GuiContext context,
                                               @NotNull Player viewer,
                                               @NotNull Map<String, String> placeholders) {
        Inventory inventory = Bukkit.createInventory(viewer, context.height() * 9, context.title());
        context.setInventory(inventory);
        context.setViewer(viewer);

        context.componentsDescending().forEach(event -> event.onViewInit(placeholders));
        context.render();
        return inventory;
    }
}
