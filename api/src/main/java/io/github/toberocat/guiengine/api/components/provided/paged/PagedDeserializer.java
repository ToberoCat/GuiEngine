package io.github.toberocat.guiengine.api.components.provided.paged;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.render.RenderPriority;

import java.io.IOException;
import java.util.Arrays;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

/**
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class PagedDeserializer extends JsonDeserializer<PagedComponent> {
    @Override
    public PagedComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        return new PagedComponentBuilder()
                .setHidden(getOptionalBoolean(node, "hidden").orElse(false))
                .setParent(node)
                .setPattern(getOptionalString(node, "pattern").map(x -> Arrays.stream(x.split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray())
                        .orElse(new int[0]))
                .setHeight(getOptionalInt(node, "height").orElse(1))
                .setWidth(getOptionalInt(node, "width").orElse(1))
                .setId(getOptionalString(node, "id").orElseThrow())
                .setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL))
                .setOffsetX(getOptionalInt(node, "x").orElse(0))
                .setOffsetY(getOptionalInt(node, "y").orElse(0))
                .setShowingPage(getOptionalInt(node, "showing-page").orElse(0))
                .setCopyAir(getOptionalBoolean(node, "copy-air").orElse(true))
                .setInteractions(getOptionalBoolean(node, "interactions").orElse(true))
                .createPagedComponent();
    }
}
