package io.github.toberocat.guiengine.function

import io.github.toberocat.guiengine.context.GuiContext

/**
 * Interface for defining custom compute functions for GUI placeholders.
 *
 *
 * Created: 30.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiComputeFunction {
    /**
     * Computes the value of the provided placeholder.
     *
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the placeholder to compute.
     * @return The computed value of the placeholder.
     */
    fun compute(context: GuiContext, value: String): String

    /**
     * Checks if the provided value contains the function to be computed.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    fun checkForFunction(value: String): Boolean
}
