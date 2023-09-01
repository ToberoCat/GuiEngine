package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException

class XmlGuiDeserializer : JsonDeserializer<XmlGui>() {
    @Throws(IOException::class)
    override fun deserialize(
        p: JsonParser,
        deserializationContext: DeserializationContext
    ): XmlGui {
        val data: MutableMap<String, JsonNode> = HashMap()
        val node = p.codec.readTree<JsonNode>(p)
        val fields = node.fieldNames()
        val interpreter = node["interpreter"].textValue()
        while (fields.hasNext()) {
            val field = fields.next()
            val jsonNode = node[field]
            data[field] = jsonNode
        }
        return XmlGui(interpreter, data)
    }
}
