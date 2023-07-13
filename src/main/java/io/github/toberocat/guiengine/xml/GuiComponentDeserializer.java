package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.GuiComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created: 10.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentDeserializer<C extends GuiComponent, B extends GuiComponentBuilder> extends JsonDeserializer<C> {
    private final @NotNull Class<B> builderClazz;

    public GuiComponentDeserializer(@NotNull Class<B> builderClazz) {
        this.builderClazz = builderClazz;
    }

    @Override
    public C deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        B builder;
        try {
            builder = builderClazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Builder " + builderClazz.getName() + " has no default constructor (0 arguments)", e);
        }
        builder.deserialize(p.getCodec().readTree(p));
        return (C) builder.createComponent();
    }
}
