package io.github.toberocat.guiengine.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Custom GUI function to add components to the GUI.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = AddComponentsFunction.Deserializer.class)
public record AddComponentsFunction(@NotNull JsonNode root) implements GuiFunction {

    public static final String ID = "add";

    /**
     * Calls the `addComponents` method using the provided API and context to add components to the GUI.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     */
    @Override
    public void call(@NotNull GuiEngineApi api, @NotNull GuiContext context) {
        try {
            addComponents(api, context, root.get("component"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recursive method to add components to the GUI from the JSON node.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context to which components will be added.
     * @param root    The JSON node representing the root component or an array of components to be added.
     * @throws JsonProcessingException If there is an issue processing the JSON data.
     */
    private void addComponents(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull JsonNode root) throws JsonProcessingException {
        if (root.isArray()) {
            for (JsonNode node : root)
                context.add(api, context.interpreter().xmlComponent(node, api));
            return;
        }

        context.add(api, context.interpreter().xmlComponent(root, api));
    }

    /**
     * Custom deserializer to convert JSON data into an `AddComponentsFunction` instance.
     */
    protected static class Deserializer extends JsonDeserializer<AddComponentsFunction> {

        @Override
        public @NotNull AddComponentsFunction deserialize(@NotNull JsonParser p, DeserializationContext ctxt) throws IOException {
            return new AddComponentsFunction(p.getCodec().readTree(p));
        }
    }
}
