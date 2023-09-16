package io.github.toberocat.guiengine.function.compute

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

    override fun compute(context: GuiContext, value: String): String {
        val permission = value.replace(PREFIX, "")
        return (null == context.viewer() || !context.viewer()!!.hasPermission(permission)).toString()
    }

    override fun checkForFunction(value: String): Boolean {
        return value.startsWith(PREFIX)
    }

    companion object {
        private const val PREFIX = "!@"
    }
}
