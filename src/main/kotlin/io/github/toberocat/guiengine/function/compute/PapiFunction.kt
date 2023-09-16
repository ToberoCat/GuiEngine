package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiComputeFunction
import me.clip.placeholderapi.PlaceholderAPI

class PapiFunction : GuiComputeFunction {
    override fun compute(context: GuiContext, value: String) =
        context.viewer()?.let { PlaceholderAPI.setPlaceholders(it, value) } ?: value

    override fun checkForFunction(value: String) = PlaceholderAPI.containsPlaceholders(value)
}