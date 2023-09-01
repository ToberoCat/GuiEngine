package io.github.toberocat.guiengine.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Utility class containing various helper methods for GUI development.
 */
object Utils {
    val threadPool: ExecutorService = Executors.newCachedThreadPool()

    /**
     * Translates the x and y coordinates of a 2D grid to a slot index in a 1D inventory slot array (9 slots per row).
     *
     * @param x The x-coordinate (column index) of the grid.
     * @param y The y-coordinate (row index) of the grid.
     * @return The corresponding slot index in the inventory.
     */
    fun translateToSlot(x: Int, y: Int): Int = y * 9 + x

    /**
     * Translates a slot index in a 1D inventory slot array (9 slots per row) to x and y coordinates of a 2D grid.
     *
     * @param slot The slot index in the inventory.
     * @return A CoordinatePair representing the x and y coordinates in the grid.
     */
    fun translateFromSlot(slot: Int): Pair<Int, Int> = slot % 9 to slot / 9
}
