package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
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
data class XmlGui(val interpreter: String, val fields: Map<String, JsonNode>) {
    operator fun get(fieldName: String): Optional<JsonNode> {
        return Optional.ofNullable(fields[fieldName])
    }

    /**
     * An array of XmlComponent objects that define the components present in the GUI.
     */
    @JacksonXmlProperty(localName = "component")
    @JacksonXmlElementWrapper(useWrapping = false)
    var components: Array<XmlComponent> = emptyArray()
}