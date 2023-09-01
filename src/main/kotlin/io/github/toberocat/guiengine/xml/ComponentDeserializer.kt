package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.render.RenderPriority
import java.io.IOException
import java.util.*

/**
 * Custom JSON deserializer for [XmlComponent].
 * This class is responsible for deserializing JSON data into a [XmlComponent] object.
 * It overrides the [JsonDeserializer.deserialize] method.
 * The expected JSON structure for deserialization should include "type", "priority", and "id" fields,
 * along with any other custom fields that represent data for the specific XmlComponent.
 *
 *
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
class ComponentDeserializer : JsonDeserializer<XmlComponent>() {
    /**
     * Deserialize JSON data into a [XmlComponent] object.
     * The JSON data should include type, priority, and id fields,
     * along with any other custom fields that represent data for the specific XmlComponent.
     *
     * @param p    The [JsonParser] to read the JSON data from.
     * @param ctxt The [DeserializationContext] to use during deserialization.
     * @return A [XmlComponent] object created from the JSON data.
     * @throws IOException If an I/O error occurs during JSON parsing.
     */
    @Throws(IOException::class)
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext
    ): XmlComponent {
        val data: MutableMap<String, JsonNode?> = HashMap()
        val node = p.codec.readTree<JsonNode>(p)
        val fields = node.fieldNames()
        val type = node["type"].textValue()
        val priority =
            if (node.has("priority"))
                RenderPriority.valueOf(node["priority"].textValue())
            else RenderPriority.NORMAL

        while (fields.hasNext()) {
            val field = fields.next()
            val jsonNode = node[field]
            data[field] = jsonNode
        }

        val id = node["id"]
        val componentId = if (null == id) UUID.randomUUID().toString() else id.textValue()
        return XmlComponent(data, priority, type, componentId)
    }
}
