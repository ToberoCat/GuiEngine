package io.github.toberocat.guiengine.api.components.provided.item;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.api.utils.JsonUtils;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class SimpleItemSerializer extends StdSerializer<SimpleItemComponent> {

    public SimpleItemSerializer() {
        super(SimpleItemComponent.class);
    }


    @Override
    public void serialize(SimpleItemComponent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        value.addParentValues(gen);

        gen.writeStringField("material", value.itemStack.getType().name());
        ItemMeta meta = value.itemStack.getItemMeta();
        if (meta != null) {
            gen.writeStringField("name", meta.getDisplayName());
            if (meta.getLore() != null)
                JsonUtils.writeArray(gen, "lore", meta.getLore().toArray());
        }
        gen.writePOJOField("on-click", value.clickFunctions);
        gen.writeEndObject();
    }
}
