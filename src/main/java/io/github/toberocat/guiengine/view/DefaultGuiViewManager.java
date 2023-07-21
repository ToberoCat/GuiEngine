package io.github.toberocat.guiengine.view;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * An implementation of the GuiViewManager interface for managing GUI views in a Minecraft plugin.
 * This class keeps track of the opened inventories and handles various inventory-related events.
 */
public class DefaultGuiViewManager implements Listener, GuiViewManager {

    /**
     * A map to store the opened inventories, mapped to the UUID of the player.
     */
    private final Map<UUID, GuiView> openedInventories;

    /**
     * Create a new instance of DefaultGuiViewManager.
     * It initializes the map to store opened inventories.
     */
    public DefaultGuiViewManager() {
        this.openedInventories = new HashMap<>();
    }

    /**
     * Check if the given player is currently viewing a GUI.
     *
     * @param player The UUID of the player.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    @Override
    public boolean isViewingGui(@NotNull UUID player) {
        return openedInventories.containsKey(player);
    }

    /**
     * Register a new GUI view for the given player.
     *
     * @param player  The UUID of the player.
     * @param guiView The GuiView to be registered.
     */
    @Override
    public void registerGui(@NotNull UUID player, @NotNull GuiView guiView) {
        openedInventories.put(player, guiView);
    }

    /**
     * Get the GUI view associated with the given player UUID.
     *
     * @param player The UUID of the player.
     * @return The GuiView associated with the player, or null if not found.
     */
    @Override
    public @Nullable GuiView getGuiView(@NotNull UUID player) {
        return openedInventories.get(player);
    }

    /**
     * Event handler for the InventoryClickEvent.
     * This method finds the corresponding GuiView associated with the clicked inventory
     * and calls the clickedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private void click(@NotNull InventoryClickEvent event) {
        getView(event.getWhoClicked(), event.getInventory()).ifPresent(view -> view.context().clickedComponent(event));
    }

    /**
     * Event handler for the InventoryDragEvent.
     * This method finds the corresponding GuiView associated with the dragged inventory
     * and calls the draggedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private void drag(@NotNull InventoryDragEvent event) {
        getView(event.getWhoClicked(), event.getInventory()).ifPresent(view -> view.context().draggedComponent(event));
    }

    /**
     * Event handler for the InventoryCloseEvent.
     * This method finds the corresponding GuiView associated with the closed inventory
     * and calls the closedComponent method in the GuiContext of that GuiView.
     */
    @EventHandler
    private void close(@NotNull InventoryCloseEvent event) {
        getView(event.getPlayer(), event.getInventory()).ifPresent(view -> view.context().closedComponent(event));
    }

    /**
     * Helper method to find the corresponding GuiView for a specific player and inventory.
     * It checks if the player is viewing a GUI, then searches for the GuiView associated with the given inventory.
     *
     * @param clicker   The HumanEntity that clicked/dragged/closed the inventory.
     * @param inventory The clicked/dragged/closed inventory.
     * @return An Optional containing the GuiView if found, or an empty Optional if not found.
     */
    private @NotNull Optional<GuiView> getView(@NotNull HumanEntity clicker, @NotNull Inventory inventory) {
        if (!isViewingGui(clicker.getUniqueId())) return Optional.empty();

        return openedInventories.values().stream().filter(x -> x.inventory().equals(inventory)).findAny();
    }
}
