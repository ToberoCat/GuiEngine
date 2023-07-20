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
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Custom GUI function to edit a component's property in the GUI.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = EditComponentFunction.Deserializer.class)
public record EditComponentFunction(@NotNull String target, @NotNull String property,
                                    @NotNull String value) implements GuiFunction {

    public static final String ID = "edit";

    /**
     * Calls the `editXmlComponentById` method using the provided API and context to edit a component's property in the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context in which the component exists.
     */
    @Override
    public void call(@NotNull GuiEngineApi api, @NotNull GuiContext context) {
        context.editXmlComponentById(api, target, xml -> xml.fields().put(property, new TextNode(value)));
    }

    /**
     * Custom deserializer to convert JSON data into an `EditComponentFunction` instance.
     */
    protected static class Deserializer extends JsonDeserializer<EditComponentFunction> {

        @Override
        public EditComponentFunction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new EditComponentFunction(JsonUtils.getOptionalString(new ParserContext(node, null, null), "target").orElseThrow(), JsonUtils.getOptionalString(new ParserContext(node, null, null), "attribute").orElseThrow(), JsonUtils.getOptionalString(new ParserContext(node, null, null), "set-value").orElseThrow());
        }
    }
}
