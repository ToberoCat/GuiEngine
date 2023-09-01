package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.toberocore.action.Actions
import org.bukkit.Bukkit
import java.io.IOException

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
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the action is called.
     */
    override fun call(api: GuiEngineApi, context: GuiContext) {
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
    class Deserializer : JsonDeserializer<ActionFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ActionFunction {
            val node = p.codec.readTree<JsonNode>(p)
            return ActionFunction(node[""].textValue())
        }
    }

    companion object {
        const val TYPE = "action"
    }
}
