package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.GuiComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 10.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentDeserializer<C extends GuiComponent> extends JsonDeserializer<C> {
    private final @NotNull GuiComponentBuilder.Factory<GuiComponentBuilder> factory;

    public GuiComponentDeserializer(@NotNull GuiComponentBuilder.Factory<GuiComponentBuilder> factory) {
        this.factory = factory;
    }

    @Override
    public C deserialize(JsonParser p,
                         DeserializationContext deserializationContext) throws IOException {
        GuiComponentBuilder builder = factory.createBuilder();
        factory.deserialize(p.getCodec().readTree(p), builder);
        return (C) builder.createComponent();
    }
}
