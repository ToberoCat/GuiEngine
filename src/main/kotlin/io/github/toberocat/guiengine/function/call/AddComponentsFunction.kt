package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import java.io.IOException

/**
 * Custom GUI function to add components to the GUI.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = AddComponentsFunction.Deserializer::class)
data class AddComponentsFunction(val root: JsonNode) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the `addComponents` method using the provided API and context to add components to the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     */
    override fun call(api: GuiEngineApi, context: GuiContext) {
        try {
            addComponents(api, context, root["component"])
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Recursive method to add components to the GUI from the JSON node.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     * @param root    The JSON node representing the root component or an array of components to be added.
     * @throws JsonProcessingException If there is an issue processing the JSON data.
     */
    @Throws(JsonProcessingException::class)
    private fun addComponents(api: GuiEngineApi, context: GuiContext, root: JsonNode) {
        if (root.isArray) {
            for (node in root) context.add(api, context.interpreter().xmlComponent(node, api))
            return
        }
        context.add(api, context.interpreter().xmlComponent(root, api))
    }

    /**
     * Custom deserializer to convert JSON data into an `AddComponentsFunction` instance.
     */
    class Deserializer : JsonDeserializer<AddComponentsFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AddComponentsFunction {
            return AddComponentsFunction(p.codec.readTree(p))
        }
    }

    companion object {
        const val TYPE = "add"
    }
}
