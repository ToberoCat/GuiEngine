package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.node.TextNode
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.xml.parsing.ParserContext
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
     * @param context The `GuiContext` instance representing the GUI context in which the component exists.
     */
    override fun call(context: GuiContext) {
        context.editXmlComponentById(target) { it.fields[attribute] = TextNode(value) }
    }

    /**
     * Custom deserializer to convert JSON data into an `EditComponentFunction` instance.
     */
    class Deserializer : JsonDeserializer<EditComponentFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): EditComponentFunction {
            val context = ParserContext.empty(p.codec.readTree(p))
            return EditComponentFunction(
                context.string("target").require(TYPE, javaClass),
                context.string("attribute").require(TYPE, javaClass),
                context.string("set-value").require(TYPE, javaClass)
            )
        }
    }

    companion object {
        const val TYPE = "edit"
    }
}
