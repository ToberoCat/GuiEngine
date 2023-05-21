package io.github.toberocat.guiengine.api.components.provided.toggle;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.JsonUtils;

import java.io.IOException;
import java.util.OptionalDouble;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ToggleItemDeserializer extends JsonDeserializer<ToggleItemComponent> {
    @Override
    public ToggleItemComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        return new ToggleItemComponentBuilder()
                .setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL))
                .setId(getOptionalString(node, "id").orElseThrow())
                .setOptions(getOptionalNode(node, "option").orElseThrow())
                .setSelected(getOptionalInt(node, "selected").orElseThrow())
                .setX(getOptionalInt(node, "x").orElse(0))
                .setY(getOptionalInt(node, "y").orElse(0))
                .setHidden(getOptionalBoolean(node, "hidden").orElse(false))
                .createToggleItemComponent();
    }
}
