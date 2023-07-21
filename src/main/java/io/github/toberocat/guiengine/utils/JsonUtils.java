package io.github.toberocat.guiengine.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class JsonUtils {

    /**
     * Write an Object array into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The Object array to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    public static void writeArray(@NotNull GeneratorContext gen, @NotNull String field, @NotNull Object[] array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (Object element : array)
            generator.writeObject(element);
        generator.writeEndArray();
    }

    /**
     * Write an int array into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The int array to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    public static void writeArray(@NotNull GeneratorContext gen, @NotNull String field, int[] array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (int element : array)
            generator.writeNumber(element);
        generator.writeEndArray();
    }

    /**
     * Write a List of Objects into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The List of Objects to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    public static void writeArray(@NotNull GeneratorContext gen, @NotNull String field, @NotNull List<Object> array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (Object element : array)
            generator.writeObject(element);
        generator.writeEndArray();
    }

    /**
     * Get an optional node from the given parent node based on the specified field name.
     *
     * @param node  The parent node to retrieve the optional node from.
     * @param field The field name of the optional node.
     * @return An Optional containing the node if found, or an empty Optional if not present.
     */
    public static @NotNull Optional<ParserContext> getOptionalNode(@NotNull ParserContext node, @NotNull String field) {
        return Optional.ofNullable(node.get(field));
    }
}
