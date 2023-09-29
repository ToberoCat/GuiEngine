package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.components.GuiComponentBuilder
import io.github.toberocat.guiengine.exception.GuiException
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Custom JSON deserializer for a specific type of [GuiComponent].
 * This class is responsible for deserializing JSON data into a [GuiComponent] object using its builder.
 *
 * @param <C> The type of [GuiComponent] to be deserialized.
 * @param <B> The type of [GuiComponentBuilder] associated with the component to be deserialized.
 *
 *
 * Created: 10.07.2023
 * Author: Tobias Madlberger (Tobias)
</B></C> */
class GuiComponentDeserializer<C : GuiComponent, B : GuiComponentBuilder?>(private val builderClazz: Class<B>) :
    JsonDeserializer<C>() {

    /**
     * Deserialize JSON data into a [GuiComponent] object using its builder.
     *
     * @param p                      The [JsonParser] to read the JSON data from.
     * @param deserializationContext The [DeserializationContext] to use during deserialization.
     * @return A [GuiComponent] object created from the JSON data using its builder.
     * @throws IOException If an I/O error occurs during JSON parsing.
     */
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, deserializationContext: DeserializationContext): C {
        val builder: B = try {
            builderClazz.getConstructor().newInstance()
        } catch (ex: Exception) {
            when (ex) {
                is InstantiationException, is IllegalAccessException, is InvocationTargetException, is NoSuchMethodException ->
                    throw RuntimeException("Builder ${builderClazz.name} has no default constructor (0 arguments)", ex)

                else -> throw ex
            }
        }

        val node = p.codec.readTree<JsonNode>(p)
        val apiId = node["__:api:__"].asText()
        val contextId = UUID.fromString(node["__:ctx:__"].asText())
        val api = GuiEngineApi.APIS[apiId]
            ?: throw GuiException("Couldn't parse component. Interpreter didn't specify '__:api:__' to a correct api id")
        val context = GuiEngineApi.LOADED_CONTEXTS[contextId]
            ?: throw GuiException("Couldn't parse component. Value received: $contextId. Interpreter didn't specify '__:ctx:__' to a correct context id")

        val computables: MutableMap<String, String> = mutableMapOf()
        builder!!.deserialize(ParserContext(node, computables, context, api))

        val component = builder.createComponent() as C
        computables.forEach { (field, value) ->
            context.computableFunctionProcessor.markAsComputed(
                component,
                field,
                value
            )
        }

        return component
    }
}
