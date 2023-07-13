package io.github.toberocat.guiengine.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.TextNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = EditComponentFunction.Deserializer.class)
public record EditComponentFunction(@NotNull String target,
                                    @NotNull String property,
                                    @NotNull String value) implements GuiFunction {

    public static final String ID = "edit";
    @Override
    public void call(@NotNull GuiEngineApi api,
                     @NotNull GuiContext context) {
        context.editXmlComponentById(api, target, xml -> xml.fields().put(property, new TextNode(value)));
    }

    protected static class Deserializer extends JsonDeserializer<EditComponentFunction> {

        @Override
        public EditComponentFunction deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new EditComponentFunction(
                    JsonUtils.getOptionalString(node, "target").orElseThrow(),
                    JsonUtils.getOptionalString(node, "attribute").orElseThrow(),
                    JsonUtils.getOptionalString(node, "set-value").orElseThrow()
            );
        }
    }
}
