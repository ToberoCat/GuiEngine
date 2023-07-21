package io.github.toberocat.guiengine.view;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * An interface representing a manager for GUI views in a Minecraft plugin.
 */
public interface GuiViewManager {

    /**
     * Get the GUI view associated with the given player UUID.
     *
     * @param player The UUID of the player.
     * @return The GuiView associated with the player, or null if not found.
     */
    @Nullable GuiView getGuiView(@NotNull UUID player);

    /**
     * Check if the given player is currently viewing a GUI.
     *
     * @param player The UUID of the player.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    boolean isViewingGui(@NotNull UUID player);

    /**
     * Register a new GUI view for the given player.
     *
     * @param player  The UUID of the player.
     * @param guiView The GuiView to be registered.
     */
    void registerGui(@NotNull UUID player, @NotNull GuiView guiView);

    /**
     * Get the GUI view associated with the given Bukkit Player object.
     * This is a default method that delegates to the UUID-based version of getGuiView.
     *
     * @param player The Bukkit Player object.
     * @return The GuiView associated with the player, or null if not found.
     */
    default @Nullable GuiView getGuiView(@NotNull Player player) {
        return getGuiView(player.getUniqueId());
    }

    /**
     * Check if the given Bukkit Player object is currently viewing a GUI.
     * This is a default method that delegates to the UUID-based version of isViewingGui.
     *
     * @param player The Bukkit Player object.
     * @return true if the player is viewing a GUI, false otherwise.
     */
    default boolean isViewingGui(@NotNull Player player) {
        return isViewingGui(player.getUniqueId());
    }
}
