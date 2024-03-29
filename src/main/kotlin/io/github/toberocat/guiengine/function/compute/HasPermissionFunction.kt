package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiComputeFunction

/**
 * Custom compute function to check if a player has a specific permission.
 *
 *
 * Created: 14.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class HasPermissionFunction : GuiComputeFunction {

    override fun compute(context: GuiContext, value: String): String {
        val permission = value.replace(PREFIX, "")
        return (null != context.viewer() && context.viewer()!!.hasPermission(permission)).toString()
    }

    override fun checkForFunction(value: String): Boolean {
        return value.startsWith(PREFIX)
    }

    companion object {
        private const val PREFIX = "@"
    }
}
