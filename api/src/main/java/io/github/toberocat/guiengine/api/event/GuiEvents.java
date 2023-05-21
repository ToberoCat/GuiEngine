package io.github.toberocat.guiengine.api.event;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiEvents {
    default void clickedComponent(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }

    default void draggedComponent(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
    }

    default void closedComponent(@NotNull InventoryCloseEvent event) {
    }

    default void onViewInit(@NotNull Map<String, String> placeholders) {

    }
}
