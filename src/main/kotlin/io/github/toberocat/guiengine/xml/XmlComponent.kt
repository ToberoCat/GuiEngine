package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.render.RenderPriority
import java.util.function.Function

/**
 * Represents an XML component to be deserialized from JSON using Jackson.
 * This class defines a record to store data for an XML component. It is annotated with
 * [JsonDeserialize] to use the [ComponentDeserializer] for deserialization.
 *
 *
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ComponentDeserializer::class)
data class XmlComponent(
    val fields: MutableMap<String, JsonNode?>,
    val renderPriority: RenderPriority,
    val type: String,
    val id: String
) {
    /**
     * Convert the fields of this  to a map of objects.
     * This method is used to transform the stored JSON data to a map of objects,
     * where each field's value is converted to its corresponding Java object representation.
     * The method also adds additional properties like "id", "type", and "renderPriority" to the map.
     *
     * @param api         The [GuiEngineApi] instance to use for object conversion.
     * @param transformer The [Function] to apply to each JSON value before conversion to an object.
     * @return A map containing the fields of this  as Java objects, including additional properties.
     */
    fun objectFields(api: GuiEngineApi, transformer: Function<JsonNode?, JsonNode?>): MutableMap<String, Any> {
        val objectMap: MutableMap<String, Any> = HashMap()
        for ((key, value1) in fields) {
            val prop = transformer.apply(value1)
            val value = api.xmlMapper.convertValue(prop, Any::class.java)
            objectMap[key] = value
        }
        objectMap["id"] = id
        objectMap["type"] = type
        objectMap["renderPriority"] = renderPriority
        return objectMap
    }
}
