package io.github.toberocat.guiengine.utils

import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
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


    fun findNodePath(currentNode: JsonNode?, targetFieldName: String): String {
        if (currentNode == null) return ""

        if (currentNode.isObject) {
            val targetNode = currentNode[targetFieldName]
            if (targetNode != null) return "/$targetFieldName"
        }

        when {
            currentNode.isArray -> {
                for (i in 0 until currentNode.size()) {
                    val arrayElement = currentNode[i]
                    val path = findNodePath(arrayElement, targetFieldName)
                    if (path.isNotEmpty()) return "[$i]$path"
                }
            }

            currentNode.isObject -> {
                val fieldNames = currentNode.fieldNames()
                while (fieldNames.hasNext()) {
                    val fieldName = fieldNames.next()
                    val fieldNode = currentNode[fieldName]
                    val path = findNodePath(fieldNode, targetFieldName)
                    if (path.isNotEmpty()) return "/$fieldName$path"
                }
            }
        }
        return ""
    }

}
