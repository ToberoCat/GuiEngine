package io.github.toberocat.guiengine.utils

import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException
import java.util.*

/**
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
object JsonUtils {
    /**
     * Write an Object array into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The Object array to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    @Throws(IOException::class)
    fun writeArray(gen: GeneratorContext, field: String, array: Array<Any?>) {
        val generator = gen.generator
        generator.writeArrayFieldStart(field)
        for (element in array) generator.writeObject(element)
        generator.writeEndArray()
    }

    /**
     * Write an int array into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The int array to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    @Throws(IOException::class)
    fun writeArray(gen: GeneratorContext, field: String, array: IntArray) {
        val generator = gen.generator
        generator.writeArrayFieldStart(field)
        for (element in array) generator.writeNumber(element)
        generator.writeEndArray()
    }

    /**
     * Write a List of Objects into a JSON array field.
     *
     * @param gen   The generator context to write JSON data.
     * @param field The field name of the JSON array.
     * @param array The List of Objects to write into the JSON array.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    @Throws(IOException::class)
    fun writeArray(gen: GeneratorContext, field: String, array: List<Any?>) {
        val generator = gen.generator
        generator.writeArrayFieldStart(field)
        for (element in array) generator.writeObject(element)
        generator.writeEndArray()
    }

    /**
     * Get an optional node from the given parent node based on the specified field name.
     *
     * @param node  The parent node to retrieve the optional node from.
     * @param field The field name of the optional node.
     * @return An Optional containing the node if found, or an empty Optional if not present.
     */
    fun getOptionalNode(node: ParserContext, field: String): Optional<ParserContext> {
        return Optional.ofNullable(node[field])
    }
}
