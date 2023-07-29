package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Custom JSON serializer for a specific type of {@link GuiComponent}.
 * This class is responsible for serializing a {@link GuiComponent} object to JSON.
 *
 * @param <C> The type of {@link GuiComponent} to be serialized.
 *            <p>
 *            Created: 10.07.2023
 *            Author: Tobias Madlberger (Tobias)
 */
public class GuiComponentSerializer<C extends GuiComponent> extends StdSerializer<C> {

    /**
     * Constructor for the GuiComponentSerializer.
     *
     * @param t The class of the {@link GuiComponent} to be serialized.
     */
    public GuiComponentSerializer(Class<C> t) {
        super(t);
    }

    /**
     * Serialize a {@link GuiComponent} object to JSON.
     *
     * @param c                  The {@link GuiComponent} object to be serialized.
     * @param jsonGenerator      The {@link JsonGenerator} to write the JSON data to.
     * @param serializerProvider The {@link SerializerProvider} to use during serialization.
     * @throws IOException If an I/O error occurs during JSON generation.
     */
    @Override
    public void serialize(@NotNull C c, @NotNull JsonGenerator jsonGenerator, @NotNull SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        c.serialize(new GeneratorContext(jsonGenerator), serializerProvider);
        jsonGenerator.writeEndObject();
    }
}
