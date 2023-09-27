package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext

/**
 * Custom GUI function to add components to the GUI.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = AddComponentsFunction.Deserializer::class)
data class AddComponentsFunction(val root: ParserContext) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the `addComponents` method using the provided API and context to add components to the GUI.
     *
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     */
    override fun call(context: GuiContext) {
        try {
            addComponents(context, root.node["component"])
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        context.render()
    }

    /**
     * Recursive method to add components to the GUI from the JSON node.
     *
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     * @param root    The JSON node representing the root component or an array of components to be added.
     * @throws JsonProcessingException If there is an issue processing the JSON data.
     */
    @Throws(JsonProcessingException::class)
    private fun addComponents(context: GuiContext, root: JsonNode) {
        if (root.isArray) {
            for (node in root) context.add(context.interpreter().xmlComponent(node, context.api))
            return
        }
        context.add(context.interpreter().xmlComponent(root, context.api))
    }

    /**
     * Custom deserializer to convert JSON data into an `AddComponentsFunction` instance.
     */
    class Deserializer : GuiFunctionFactory<AddComponentsFunction>() {
        override fun build(node: ParserContext) = AddComponentsFunction(node)
    }

    companion object {
        const val TYPE = "add"
    }
}
