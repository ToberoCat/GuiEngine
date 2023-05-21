package io.github.toberocat.guiengine.api.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = AddComponentsFunction.Deserializer.class)
public record AddComponentsFunction(@NotNull JsonNode root) implements GuiFunction {

    public static final String ID = "add";

    @Override
    public void call(@NotNull GuiEngineApi api,
                     @NotNull GuiContext context) {
        try {
            addComponents(api, context, root.get("component"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addComponents(@NotNull GuiEngineApi api,
                               @NotNull GuiContext context,
                               @NotNull JsonNode root) throws JsonProcessingException {
        if (root.isArray()) {
            for (JsonNode node : root)
                context.add(api, context.interpreter().xmlComponent(node, api));
            return;
        }

        context.add(api, context.interpreter().xmlComponent(root, api));
    }

    protected static class Deserializer extends JsonDeserializer<AddComponentsFunction> {

        @Override
        public AddComponentsFunction deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return new AddComponentsFunction(p.getCodec().readTree(p));
        }
    }
}
