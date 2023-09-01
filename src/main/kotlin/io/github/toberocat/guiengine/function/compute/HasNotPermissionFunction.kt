package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.ComputeFunction

/**
 * Custom compute function to check if a player does not have a specific permission.
 *
 *
 * Created: 14.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class HasNotPermissionFunction : ComputeFunction {
    /**
     * Computes the result of the permission check using the provided API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the permission node to check.
     * @return A string representation of the result with the permission check.
     * `True` if the player does not have the specified permission, `false` otherwise.
     */
    override fun compute(api: GuiEngineApi, context: GuiContext, value: String): String {
        val permission = value.replace(PREFIX, "")
        return (null == context.viewer() || !context.viewer()!!.hasPermission(permission)).toString()
    }

    /**
     * Checks if the provided value contains the function to check for the absence of a permission.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    override fun checkForFunction(value: String): Boolean {
        return value.startsWith(PREFIX)
    }

    companion object {
        private const val PREFIX = "!@"
    }
}
