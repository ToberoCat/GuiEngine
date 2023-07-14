package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.utils.GeneratorContext;

import java.io.IOException;

/**
 * Created: 10.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentSerializer<C extends GuiComponent> extends StdSerializer<C> {
    public GuiComponentSerializer(Class<C> t) {
        super(t);
    }

    @Override
    public void serialize(C c,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        c.serialize(new GeneratorContext(jsonGenerator), serializerProvider);
        jsonGenerator.writeEndObject();
    }
}
