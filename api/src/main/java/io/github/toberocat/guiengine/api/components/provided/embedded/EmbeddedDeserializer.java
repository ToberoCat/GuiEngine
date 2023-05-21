package io.github.toberocat.guiengine.api.components.provided.embedded;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;

import java.io.IOException;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class EmbeddedDeserializer extends JsonDeserializer<EmbeddedGuiComponent> {
    @Override
    public EmbeddedGuiComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        return new EmbeddedGuiComponentBuilder()
                .setId(getOptionalString(node, "id").orElseThrow())
                .setX(getOptionalInt(node, "x").orElse(0))
                .setY(getOptionalInt(node, "y").orElse(0))
                .setWidth(getOptionalInt(node, "width").orElseThrow())
                .setHeight(getOptionalInt(node, "height").orElseThrow())
                .setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL))
                .setTargetGui(getOptionalString(node, "target-gui").orElseThrow())
                .setCopyAir(getOptionalBoolean(node, "copy-air").orElse(true))
                .setInteractions(getOptionalBoolean(node, "interactions").orElse(true))
                .setHidden(getOptionalBoolean(node, "hidden").orElse(false))
                .createEmbeddedGuiComponent();
    }
}
