package io.github.toberocat.guiengine.api.view;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class DefaultGuiViewManager implements Listener, GuiViewManager {
    private final Map<UUID, GuiView> openedInventories;

    public DefaultGuiViewManager() {
        this.openedInventories = new HashMap<>();
    }

    @Override
    public boolean isViewingGui(@NotNull UUID player) {
        return openedInventories.containsKey(player);
    }

    @Override
    public void registerGui(@NotNull UUID player, @NotNull GuiView guiView) {
        openedInventories.put(player, guiView);

    }

    @Override
    public @Nullable GuiView getGuiView(@NotNull UUID player) {
        return openedInventories.get(player);
    }


    @EventHandler
    private void click(@NotNull InventoryClickEvent event) {
        getView(event.getWhoClicked(), event.getInventory())
                .ifPresent(view -> view.context().clickedComponent(event));
    }

    @EventHandler
    private void drag(@NotNull InventoryDragEvent event) {
        getView(event.getWhoClicked(), event.getInventory())
                .ifPresent(view -> view.context().draggedComponent(event));
    }

    @EventHandler
    private void close(@NotNull InventoryCloseEvent event) {
        getView(event.getPlayer(), event.getInventory())
                .ifPresent(view -> view.context().closedComponent(event));
    }

    private @NotNull Optional<GuiView> getView(@NotNull HumanEntity clicker, @NotNull Inventory inventory) {
        if (!isViewingGui(clicker.getUniqueId()))
            return Optional.empty();

        return openedInventories.values()
                .stream()
                .filter(x -> x.inventory().equals(inventory))
                .findAny();
    }
}