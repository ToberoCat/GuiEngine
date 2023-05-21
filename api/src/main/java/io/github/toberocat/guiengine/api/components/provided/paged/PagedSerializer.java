package io.github.toberocat.guiengine.api.components.provided.paged;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.api.components.provided.toggle.ToggleItemComponent;
import io.github.toberocat.guiengine.api.utils.JsonUtils;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class PagedSerializer extends StdSerializer<PagedComponent> {

    public PagedSerializer() {
        super(PagedComponent.class);
    }


    @Override
    public void serialize(PagedComponent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        value.addParentValues(gen);
        JsonUtils.writeArray(gen, "pattern", value.pattern);
        gen.writeNumberField("showing-page", value.showingPage);
        gen.writeRaw(value.parent.toString());
        gen.writeEndObject();
    }
}
