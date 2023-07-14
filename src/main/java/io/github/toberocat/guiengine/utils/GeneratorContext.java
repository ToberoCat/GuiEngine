package io.github.toberocat.guiengine.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 14.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public record GeneratorContext(@NotNull JsonGenerator generator) {

    @Override
    public JsonGenerator generator() {
        return generator;
    }

    public void writeStringField(@NotNull String fieldName, @NotNull String value) throws IOException {
        generator.writeStringField(fieldName, value);
    }

    public void writeNumberField(@NotNull String fieldName, int value) throws IOException {
        generator.writeNumberField(fieldName, value);
    }

    public void writeBooleanField(@NotNull String fieldName, boolean value) throws IOException {
        generator.writeBooleanField(fieldName, value);
    }

    public void writePOJOField(@NotNull String fieldName, Object value) throws IOException {
        generator.writePOJOField(fieldName, value);
    }

    public void writeRaw(@NotNull String raw) throws IOException {
        generator.writeRaw(raw);
    }
}
