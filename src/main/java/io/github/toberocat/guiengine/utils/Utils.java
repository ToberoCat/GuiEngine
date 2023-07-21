package io.github.toberocat.guiengine.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class containing various helper methods for GUI development.
 */
public final class Utils {

    /**
     * Translates the x and y coordinates of a 2D grid to a slot index in a 1D inventory slot array (9 slots per row).
     *
     * @param x The x-coordinate (column index) of the grid.
     * @param y The y-coordinate (row index) of the grid.
     * @return The corresponding slot index in the inventory.
     */
    public static int translateToSlot(int x, int y) {
        return y * 9 + x;
    }

    /**
     * Translates a slot index in a 1D inventory slot array (9 slots per row) to x and y coordinates of a 2D grid.
     *
     * @param slot The slot index in the inventory.
     * @return A CoordinatePair representing the x and y coordinates in the grid.
     */
    public static @NotNull CoordinatePair translateFromSlot(int slot) {
        return new CoordinatePair(slot % 9, slot / 9);
    }
}
