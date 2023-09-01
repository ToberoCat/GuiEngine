package io.github.toberocat.guiengine.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.function.FunctionProcessor.applyFunctions
import io.github.toberocat.guiengine.function.FunctionProcessor.createFunction
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import org.bukkit.Material
import java.util.*
import java.util.function.Consumer

data class ParserContext(
    val node: JsonNode,
    private val context: GuiContext?,
    private val api: GuiEngineApi?
) {

    /**
     * Get a child node of the current node based on the specified field name.
     *
     * @param field The field name of the child node to retrieve.
     * @return A ParserContext representing the child node if found, or null if not present.
     */
    operator fun get(field: String): ParserContext? = when {
        node.has(field) -> ParserContext(node.get(field), context, api)
        else -> null
    }


    /**
     * Get an optional child node of the current node based on the specified field name.
     *
     * @param field The field name of the optional child node to retrieve.
     * @return An Optional containing the child node if found, or an empty Optional if not present.
     */
    fun getOptionalNode(field: String): Optional<ParserContext> = Optional.ofNullable(get(field))

    /**
     * Read a node's children as a list of ParserContext objects.
     * If the current node is an array, each element will be represented as a separate ParserContext object.
     * If the current node is not an array, the current node itself will be the only element in the list.
     *
     * @return A list of ParserContext objects representing the node's children.
     */
    fun getFieldList(): List<ParserContext> {
        val children: MutableList<ParserContext> = ArrayList()
        if (node.isArray) {
            for (n in node) children.add(ParserContext(n!!, context!!, api!!))
        } else {
            children.add(this)
        }
        return children
    }

    /**
     * Get an optional list of ParserContext objects from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional list.
     * @return An Optional containing the list of ParserContext objects if found,
     * or an empty Optional if not present.
     */
    fun getOptionalFieldList(field: String): Optional<List<ParserContext>> {
        return getOptionalNode(field).map {
            val children: MutableList<ParserContext> = ArrayList()
            if (it.node.isArray) {
                for (n in it.node) children.add(ParserContext(n!!, context, api))
            } else {
                children.add(it)
            }
            children
        }
    }

    /**
     * Get an optional Material enum value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Material.
     * @return An Optional containing the Material enum value if found,
     * or an empty Optional if not present.
     * @throws InvalidGuiComponentException If the provided material doesn't match any known materials.
     */
    fun getOptionalMaterial(field: String): Optional<Material> = getOptionalEnum<Material>(Material::class.java, field)

    inline fun <reified T : Enum<T>> getOptionalEnum(clazz: Class<T>, field: String): Optional<T> =
        getOptionalString(field).map { x ->
            try {
                enumValueOf(x)
            } catch (e: IllegalArgumentException) {
                throw InvalidGuiComponentException(
                    "The provided ${clazz.simpleName} '$x' doesn't " + "match any possible values. " + "Values allowed: ${clazz.enumConstants.joinToString { it.name }}"
                )
            }
        }

    /**
     * Get an optional RenderPriority enum value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional RenderPriority.
     * @return An Optional containing the RenderPriority enum value if found,
     * or an empty Optional if not present.
     */
    fun getOptionalRenderPriority(field: String): Optional<RenderPriority> =
        getOptionalEnum(RenderPriority::class.java, field)

    /**
     * Get an optional String value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String.
     * @return An Optional containing the String value if found,
     * or an empty Optional if not present.
     */
    fun getOptionalString(field: String): Optional<String> = getOptionalNode(field)
        .map { it.node.asText() }
        .map { x -> api?.let { context?.let { applyFunctions(api, context, x) } } ?: x }

    /**
     * Get an optional UUID value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional UUID.
     * @return An Optional containing the UUID value if found,
     * or an empty Optional if not present.
     */
    fun getOptionalUUID(field: String): Optional<UUID> {
        return getOptionalString(field).map {
            try {
                return@map UUID.fromString(it)
            } catch (_: IllegalArgumentException) {
                return@map null
            }
        }
    }

    /**
     * Get an optional Boolean value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Boolean.
     * @return An Optional containing the Boolean value if found,
     * or an empty Optional if not present.
     */
    fun getOptionalBoolean(field: String): Optional<Boolean> {
        return getOptionalString(field).map { anObject: String? ->
            "true".equals(
                anObject
            )
        }
    }

    /**
     * Get an optional Integer value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Integer.
     * @return An Optional containing the Integer value if found,
     * or an empty Optional if not present.
     */
    fun getOptionalInt(field: String): Optional<Int> {
        return getOptionalString(field).map { s: String -> s.toInt() }
    }

    /**
     * Get an optional array of Strings from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String array.
     * @return An Optional containing the array of Strings if found,
     * or an empty Optional if not present.
     */
    fun getOptionalStringArray(field: String): Optional<Array<String>> {
        return getOptionalNode(field).map { node: ParserContext ->
            val array: MutableList<String> = ArrayList()
            if (!node.node.isArray) array.add(node.asText()) else node.node.forEach(Consumer { elementNode: JsonNode ->
                array.add(
                    applyFunctions(
                        api!!, context!!, elementNode.asText()
                    )
                )
            })
            array.toTypedArray<String>()
        }
    }

    /**
     * Get an optional map of String keys and ParserContext values from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional node map.
     * @return An Optional containing the map of String keys and ParserContext values if found,
     * or an empty Optional if not present.
     */
    fun getOptionalNodeMap(field: String): Optional<Map<String, ParserContext>> {
        return getOptionalNode(field).map { root: ParserContext ->
            val map: MutableMap<String, ParserContext> = HashMap()
            val fieldsIterator: Iterator<Map.Entry<String, JsonNode>> =
                root.node.fields()
            while (fieldsIterator.hasNext()) {
                val (key, value) = fieldsIterator.next()
                map[key] = ParserContext(value, context, api)
            }
            map
        }
    }

    /**
     * Get an optional list of GuiFunction objects from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional functions.
     * @return An Optional containing the list of GuiFunction objects if found,
     * or an empty Optional if not present.
     */
    fun getFunctions(field: String): Optional<List<GuiFunction>> {
        return getOptionalNode(field).map { x: ParserContext ->
            try {
                val functions: MutableList<GuiFunction> = LinkedList()
                if (x.node.isArray) {
                    for (function in x.node) functions.add(createFunction(function!!))
                } else {
                    functions.add(createFunction(x.node))
                }
                return@map functions
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun asText(): String {
        return applyFunctions(api!!, context!!, node.asText())
    }
}
