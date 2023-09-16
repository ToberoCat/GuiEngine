package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.utils.nullCheck
import java.io.IOException

class XmlGuiDeserializer : JsonDeserializer<XmlGui>() {
    @Throws(IOException::class)
    override fun deserialize(
        p: JsonParser, deserializationContext: DeserializationContext
    ): XmlGui {
        val data: MutableMap<String, JsonNode> = HashMap()
        val node = p.codec.readTree<JsonNode>(p)
        val fields = node.fieldNames()
        val mapper = GuiEngineApi.APIS[node["api"].asText()]?.xmlMapper
            .nullCheck("API field not set")
        val interpreter = when {
            node.has("interpreter") -> node["interpreter"].asText()
            else -> "default"
        }
        val components: Array<XmlComponent> = when {
            node.has("component") -> {
                val componentNode = node["component"]
                when {
                    !componentNode.isArray -> arrayOf(mapper.treeToValue(componentNode, XmlComponent::class.java))
                    else -> componentNode.map { mapper.treeToValue(it, XmlComponent::class.java) }
                        .toTypedArray()
                }
            }

            else -> emptyArray()
        }

        while (fields.hasNext()) {
            val field = fields.next()
            val jsonNode = node[field]
            data[field] = jsonNode
        }
        return XmlGui(interpreter, components, data)
    }
}
