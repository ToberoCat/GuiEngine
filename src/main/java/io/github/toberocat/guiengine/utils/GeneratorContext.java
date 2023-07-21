package io.github.toberocat.guiengine.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * GeneratorContext is a utility class that encapsulates a JsonGenerator instance to assist in writing JSON data.
 * <p>
 * Created: 14.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public record GeneratorContext(@NotNull JsonGenerator generator) {

    /**
     * Retrieves the JsonGenerator associated with this context.
     *
     * @return The JsonGenerator instance.
     */
    @Override
    public JsonGenerator generator() {
        return generator;
    }

    /**
     * Writes a JSON string field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The string value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    public void writeStringField(@NotNull String fieldName, @NotNull String value) throws IOException {
        generator.writeStringField(fieldName, value);
    }

    /**
     * Writes a JSON number field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The integer value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    public void writeNumberField(@NotNull String fieldName, int value) throws IOException {
        generator.writeNumberField(fieldName, value);
    }

    /**
     * Writes a JSON boolean field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The boolean value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    public void writeBooleanField(@NotNull String fieldName, boolean value) throws IOException {
        generator.writeBooleanField(fieldName, value);
    }

    /**
     * Writes a JSON POJO field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The object value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    public void writePOJOField(@NotNull String fieldName, Object value) throws IOException {
        generator.writePOJOField(fieldName, value);
    }

    /**
     * Writes raw JSON data to the generator.
     *
     * @param raw The raw JSON data to write.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    public void writeRaw(@NotNull String raw) throws IOException {
        generator.writeRaw(raw);
    }
}
