package io.github.toberocat.guiengine.xml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import java.io.IOException

/**
 * Custom JSON serializer for a specific type of [GuiComponent].
 * This class is responsible for serializing a [GuiComponent] object to JSON.
 *
 * @param <C> The type of [GuiComponent] to be serialized.
 *
 *
 * Created: 10.07.2023
 * Author: Tobias Madlberger (Tobias)
</C> */
class GuiComponentSerializer<C : GuiComponent>(t: Class<C>) : StdSerializer<C>(t) {

    /**
     * Serialize a [GuiComponent] object to JSON.
     *
     * @param c                  The [GuiComponent] object to be serialized.
     * @param jsonGenerator      The [JsonGenerator] to write the JSON data to.
     * @param serializerProvider The [SerializerProvider] to use during serialization.
     * @throws IOException If an I/O error occurs during JSON generation.
     */
    @Throws(IOException::class)
    override fun serialize(c: C, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeStartObject()
        c.serialize(GeneratorContext(jsonGenerator), serializerProvider)
        jsonGenerator.writeEndObject()
    }
}
