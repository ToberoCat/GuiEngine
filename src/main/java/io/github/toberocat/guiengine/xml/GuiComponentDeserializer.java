package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.GuiComponentBuilder;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * Custom JSON deserializer for a specific type of {@link GuiComponent}.
 * This class is responsible for deserializing JSON data into a {@link GuiComponent} object using its builder.
 *
 * @param <C> The type of {@link GuiComponent} to be deserialized.
 * @param <B> The type of {@link GuiComponentBuilder} associated with the component to be deserialized.
 *            <p>
 *            Created: 10.07.2023
 *            Author: Tobias Madlberger (Tobias)
 */
public class GuiComponentDeserializer<C extends GuiComponent, B extends GuiComponentBuilder> extends JsonDeserializer<C> {

    private final @NotNull Class<B> builderClazz;

    /**
     * Constructor for the GuiComponentDeserializer.
     *
     * @param builderClazz The class of the {@link GuiComponentBuilder} associated with the component to be deserialized.
     */
    public GuiComponentDeserializer(@NotNull Class<B> builderClazz) {
        this.builderClazz = builderClazz;
    }

    /**
     * Deserialize JSON data into a {@link GuiComponent} object using its builder.
     *
     * @param p                      The {@link JsonParser} to read the JSON data from.
     * @param deserializationContext The {@link DeserializationContext} to use during deserialization.
     * @return A {@link GuiComponent} object created from the JSON data using its builder.
     * @throws IOException If an I/O error occurs during JSON parsing.
     */
    @Override
    public C deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        B builder;
        try {
            builder = builderClazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Builder " + builderClazz.getName() + " has no default constructor (0 arguments)", e);
        }

        JsonNode node = p.getCodec().readTree(p);

        String apiId = node.get("__:api:__").asText();
        UUID contextId = UUID.fromString(node.get("__:ctx:__").asText());

        GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
        GuiContext context = GuiEngineApi.LOADED_CONTEXTS.get(contextId);

        builder.deserialize(new ParserContext(node, context, api));
        return (C) builder.createComponent();
    }
}
