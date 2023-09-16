package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiComputeFunction

// ToDo: Currently just for testing
class DateFunction : GuiComputeFunction {

    override fun compute(context: GuiContext, value: String): String {
        return "" + System.currentTimeMillis()
    }

    override fun checkForFunction(value: String) = value == PREFIX

    companion object {
        private const val PREFIX = ":date"
    }
}