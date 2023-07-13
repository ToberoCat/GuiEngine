package io.github.toberocat.guiengine.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@JsonDeserialize(using = RemoveComponentFunction.Deserializer.class)
public record RemoveComponentFunction(@NotNull String target) implements GuiFunction {

    public static final String ID = "remove";

    @Override
    public void call(@NotNull GuiEngineApi api,
                     @NotNull GuiContext context) {
        context.removeById(target);
    }

    protected static class Deserializer extends JsonDeserializer<RemoveComponentFunction> {

        @Override
        public RemoveComponentFunction deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new RemoveComponentFunction(
                    JsonUtils.getOptionalString(node, "target").orElseThrow()
            );
        }
    }
}
