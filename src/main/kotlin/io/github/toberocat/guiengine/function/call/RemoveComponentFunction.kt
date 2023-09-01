package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.utils.ParserContext
import java.io.IOException

/**
 * Custom GUI function to remove a component from the GUI.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = RemoveComponentFunction.Deserializer::class)
data class RemoveComponentFunction(val target: String) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the `removeById` method using the provided API and context to remove a component from the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context from which to remove the component.
     */
    override fun call(api: GuiEngineApi, context: GuiContext) {
        context.removeById(target)
    }

    /**
     * Custom deserializer to convert JSON data into a `RemoveComponentFunction` instance.
     */
    class Deserializer : JsonDeserializer<RemoveComponentFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RemoveComponentFunction {
            val node = p.codec.readTree<JsonNode>(p)
            return RemoveComponentFunction(
                ParserContext(node, null, null)
                    .getOptionalString("v").orElseThrow()
            )
        }
    }

    companion object {
        const val TYPE = "remove"
    }
}