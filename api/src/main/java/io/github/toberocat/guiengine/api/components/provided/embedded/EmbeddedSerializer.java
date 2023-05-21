package io.github.toberocat.guiengine.api.components.provided.embedded;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
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
public class EmbeddedSerializer extends StdSerializer<EmbeddedGuiComponent> {

    public EmbeddedSerializer() {
        super(EmbeddedGuiComponent.class);
    }

    @Override
    public void serialize(EmbeddedGuiComponent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        value.addParentValues(gen);

        gen.writeStringField("target-gui", value.targetGui);
        gen.writeBooleanField("copy-air", value.copyAir);
        gen.writeBooleanField("interactions", value.interactions);
        gen.writeEndObject();
    }
}
