package io.github.toberocat.guiengine.api.components.provided.head;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class HeadItemDeserializer extends JsonDeserializer<HeadItemComponent> {
    @Override
    public HeadItemComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        return new HeadItemComponentBuilder()
                .setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL))
                .setId(getOptionalString(node, "id").orElseThrow())
                .setName(getOptionalString(node, "name").orElse(" "))
                .setTextureId(getOptionalString(node, "head-texture").orElse(null))
                .setOwner(getOptionalUUID(node, "head-owner").orElse(null))
                .setLore(getOptionalStringArray(node, "lore").orElse(new String[0]))
                .setClickFunctions(getFunctions(node, "on-click").orElse(new ArrayList<>()))
                .setX(getOptionalInt(node, "x").orElse(0))
                .setY(getOptionalInt(node, "y").orElse(0))
                .setHidden(getOptionalBoolean(node, "hidden").orElse(false))
                .createHeadItemComponent();
    }
}
