package io.github.toberocat.guiengine.api.components.provided.head;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toberocat.guiengine.api.utils.JsonUtils;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class HeadItemSerializer extends StdSerializer<HeadItemComponent> {

    public HeadItemSerializer() {
        super(HeadItemComponent.class);
    }

    @Override
    public void serialize(HeadItemComponent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        value.addParentValues(gen);

        if (value.getItemStack().getItemMeta() instanceof SkullMeta skullMeta) {
            gen.writeStringField("name", skullMeta.getDisplayName());

            if (skullMeta.getOwningPlayer() != null)
                gen.writeStringField("head-owner", skullMeta.getOwningPlayer().getUniqueId()
                        .toString());

            if (skullMeta.getLore() != null)
                JsonUtils.writeArray(gen, "lore", skullMeta.getLore().toArray());
        }

        gen.writePOJOField("on-click", value.getClickFunctions());
        gen.writeEndObject();
    }
}
