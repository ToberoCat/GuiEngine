package io.github.toberocat.guiengine.xml.parsing

import com.fasterxml.jackson.core.JsonGenerator
import io.github.toberocat.guiengine.function.GuiFunction
import java.io.IOException

/**
 * GeneratorContext is a utility class that encapsulates a JsonGenerator instance to assist in writing JSON data.
 *
 *
 * Created: 14.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
data class GeneratorContext(val generator: JsonGenerator) {
    /**
     * Retrieves the JsonGenerator associated with this context.
     *
     * @return The JsonGenerator instance.
     */
    fun generator(): JsonGenerator {
        return generator
    }

    /**
     * Writes a JSON string field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The string value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeStringField(fieldName: String, value: String) {
        generator.writeStringField(fieldName, value)
    }

    /**
     * Writes a JSON number field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The integer value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeNumberField(fieldName: String, value: Int) {
        generator.writeNumberField(fieldName, value)
    }

    /**
     * Writes a JSON number field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The integer value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeNumberField(fieldName: String, value: Long) {
        generator.writeNumberField(fieldName, value)
    }

    /**
     * Writes a JSON number field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The integer value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeNumberField(fieldName: String, value: Float) {
        generator.writeNumberField(fieldName, value)
    }

    /**
     * Writes a JSON number field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The integer value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeNumberField(fieldName: String, value: Double) {
        generator.writeNumberField(fieldName, value)
    }

    /**
     * Writes a JSON boolean field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The boolean value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeBooleanField(fieldName: String, value: Boolean) {
        generator.writeBooleanField(fieldName, value)
    }

    /**
     * Writes a JSON POJO field with the specified field name and value to the generator.
     *
     * @param fieldName The name of the JSON field to write.
     * @param value     The object value to write for the field.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writePOJOField(fieldName: String, value: Any?) {
        generator.writePOJOField(fieldName, value)
    }

    @Throws(IOException::class)
    fun writeFunctionField(fieldName: String, functions: List<GuiFunction?>) {
        if (functions.size == 1) writePOJOField("on-click", functions[0]) else writePOJOField("on-click", functions)
    }

    /**
     * Writes raw JSON data to the generator.
     *
     * @param raw The raw JSON data to write.
     * @throws IOException If an I/O error occurs while writing to the generator.
     */
    @Throws(IOException::class)
    fun writeRaw(raw: String) {
        generator.writeRaw(raw)
    }
}