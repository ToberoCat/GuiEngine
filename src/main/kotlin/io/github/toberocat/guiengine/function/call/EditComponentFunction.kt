package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.node.TextNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.utils.ParserContext
import java.io.IOException

/**
 * Custom GUI function to edit a component's attribute in the GUI.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = EditComponentFunction.Deserializer::class)
data class EditComponentFunction(
    val target: String,
    val attribute: String,
    @field:JsonProperty("set-value") @param:JsonProperty("set-value") val value: String
) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the `editXmlComponentById` method using the provided API and context to edit a component's attribute in the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context in which the component exists.
     */
    override fun call(api: GuiEngineApi, context: GuiContext) {
        context.editXmlComponentById(api, target) { it.fields[attribute] = TextNode(value) }
    }

    /**
     * Custom deserializer to convert JSON data into an `EditComponentFunction` instance.
     */
    class Deserializer : JsonDeserializer<EditComponentFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): EditComponentFunction {
            val node = p.codec.readTree<JsonNode>(p)
            return EditComponentFunction(
                ParserContext(node, null, null)
                    .getOptionalString("target")
                    .orElseThrow(), ParserContext(node, null, null)
                    .getOptionalString("attribute")
                    .orElseThrow(), ParserContext(node, null, null)
                    .getOptionalString("set-value")
                    .orElseThrow()
            )
        }
    }

    companion object {
        const val TYPE = "edit"
    }
}
