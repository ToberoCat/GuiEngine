package io.github.toberocat.guiengine.api.components.provided.toggle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.api.utils.JsonUtils;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ToggleItemSerializer extends StdSerializer<ToggleItemComponent> {

    public ToggleItemSerializer() {
        super(ToggleItemComponent.class);
    }


    @Override
    public void serialize(ToggleItemComponent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        value.addParentValues(gen);
        gen.writePOJOField("option", value.getOptions());
        gen.writeNumberField("selected", value.getSelected());
        gen.writeEndObject();
    }
}
