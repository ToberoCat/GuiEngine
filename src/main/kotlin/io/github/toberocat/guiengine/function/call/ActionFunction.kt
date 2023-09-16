package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.toberocore.action.Actions
import org.bukkit.Bukkit

/**
 * Custom GUI function to call an action when triggered.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ActionFunction.Deserializer::class)
data class ActionFunction(val action: String) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the specified action using the provided API and context.
     *
     * @param context The `GuiContext` instance representing the GUI context for which the action is called.
     */
    override fun call(context: GuiContext) {
        val viewer = context.viewer() ?: return
        Bukkit.getScheduler().runTask(plugin, Runnable {
            Actions(
                action
            )
                .localActions(context.localActions)
                .run(viewer)
        })
    }

    /**
     * Custom deserializer to convert JSON data into an `ActionFunction` instance.
     */
    class Deserializer : GuiFunctionFactory<ActionFunction>() {
        override fun build(node: ParserContext): ActionFunction = ActionFunction(
            node.string("").require(TYPE, javaClass)
        )
    }

    companion object {
        const val TYPE = "action"
    }
}
