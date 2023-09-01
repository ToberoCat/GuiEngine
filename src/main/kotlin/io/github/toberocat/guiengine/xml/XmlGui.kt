package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.*

/**
 * Represents an XML-based GUI configuration.
 * This class is used
 * to deserialize an XML configuration file that defines the properties of a graphical user interface.
 * It uses Jackson's annotations to map XML elements and attributes to Java class properties during deserialization.
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = XmlGuiDeserializer::class)
data class XmlGui(
    val interpreter: String,
    val components: Array<XmlComponent>,
    val fields: Map<String, JsonNode>
) {
    operator fun get(fieldName: String): Optional<JsonNode> {
        return Optional.ofNullable(fields[fieldName])
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XmlGui

        if (interpreter != other.interpreter) return false
        if (!components.contentEquals(other.components)) return false
        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int {
        var result = interpreter.hashCode()
        result = 31 * result + components.contentHashCode()
        result = 31 * result + fields.hashCode()
        return result
    }
}