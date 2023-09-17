package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiComputeFunction
import io.github.toberocat.guiengine.utils.VirtualPlayer
import me.clip.placeholderapi.PlaceholderAPI

class PapiFunction : GuiComputeFunction {
    override fun compute(context: GuiContext, value: String): String = context.viewer()?.let {
        return@let if (it !is VirtualPlayer) PlaceholderAPI.setPlaceholders(
            it, value
        ) else null
    } ?: value

    override fun checkForFunction(value: String) = PlaceholderAPI.containsPlaceholders(value)
}