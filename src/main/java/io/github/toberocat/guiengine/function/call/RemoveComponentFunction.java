package io.github.toberocat.guiengine.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Custom GUI function to remove a component from the GUI.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = RemoveComponentFunction.Deserializer.class)
public record RemoveComponentFunction(@NotNull String target) implements GuiFunction {

    public static final String ID = "remove";

    @Override
    public @NotNull String getType() {
        return ID;
    }

    /**
     * Calls the `removeById` method using the provided API and context to remove a component from the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context from which to remove the component.
     */
    @Override
    public void call(@NotNull GuiEngineApi api, @NotNull GuiContext context) {
        context.removeById(target);
    }

    /**
     * Custom deserializer to convert JSON data into a `RemoveComponentFunction` instance.
     */
    protected static class Deserializer extends JsonDeserializer<RemoveComponentFunction> {

        @Override
        public @NotNull RemoveComponentFunction deserialize(@NotNull JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new RemoveComponentFunction(new ParserContext(node, null, null).getOptionalString("target").orElseThrow());
        }
    }
}
