package io.github.toberocat.guiengine.xml.parsing

import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.GuiIORuntimeException
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import org.bukkit.Material
import java.util.*
import java.util.function.Consumer

open class ParserContext(
    val node: JsonNode,
    internal val computables: MutableMap<String, String>,
    internal val context: GuiContext?,
    internal val api: GuiEngineApi?
) : Iterable<ParserContext> {
    companion object {
        fun empty(node: JsonNode) = ParserContext(node, mutableMapOf(), null, null)
    }

    /**
     * Get a child node of the current node based on the specified field name.
     *
     * @param field The field name of the child node to retrieve.
     * @return A ParserContext representing the child node if found, or null if not present.
     */
    open operator fun get(field: String): ParserContext? = when {
        node.has(field) -> ParserContext(node.get(field), computables, context, api)
        else -> null
    }


    /**
     * Get an optional child node of the current node based on the specified field name.
     *
     * @param field The field name of the optional child node to retrieve.
     * @return An Optional containing the child node if found, or an empty Optional if not present.
     */
    fun node(field: String): ParsingOptional<ParserContext> = ParsingOptional(field, get(field))

    /**
     * Read a node's children as a list of ParserContext objects.
     * If the current node is an array, each element will be represented as a separate ParserContext object.
     * If the current node is not an array, the current node itself will be the only element in the list.
     *
     * @return A list of ParserContext objects representing the node's children.
     */
    fun fieldList(): List<ParserContext> {
        val children = mutableListOf<ParserContext>()
        if (node.isArray) {
            for (n in node) children.add(ParserContext(n!!, computables, context, api))
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
    fun fieldList(field: String): ParsingOptional<List<ParserContext>> {
        return node(field).map {
            val children: MutableList<ParserContext> = ArrayList()
            if (it.node.isArray) {
                for (n in it.node) children.add(ParserContext(n!!, computables, context, api))
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
    fun material(field: String): ParsingOptional<Material> = enum<Material>(Material::class.java, field)

    inline fun <reified T : Enum<T>> enum(clazz: Class<T>, field: String): ParsingOptional<T> =
        string(field).mapSafe({ enumValueOf<T>(it) }, {
            InvalidGuiComponentException(
                "The provided ${clazz.simpleName} '$it' doesn't match any possible values. " + "Values allowed: ${clazz.enumConstants.joinToString { value -> value.name }}"
            )
        })

    /**
     * Get an optional RenderPriority enum value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional RenderPriority.
     * @return An Optional containing the RenderPriority enum value if found,
     * or an empty Optional if not present.
     */
    fun renderPriority(field: String): ParsingOptional<RenderPriority> = enum(RenderPriority::class.java, field)

    /**
     * Get an optional String value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String.
     * @return An Optional containing the String value if found,
     * or an empty Optional if not present.
     */
    fun string(field: String) = node(field).map { asText(field, it.node) }

    /**
     * Get an optional UUID value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional UUID.
     * @return An Optional containing the UUID value if found,
     * or an empty Optional if not present.
     */
    fun uuid(field: String): ParsingOptional<UUID> = string(field).mapSafe { UUID.fromString(it) }

    /**
     * Get an optional Boolean value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Boolean.
     * @return An Optional containing the Boolean value if found,
     * or an empty Optional if not present.
     */
    fun boolean(field: String) = string(field).map { anObject: String? -> "true" == anObject }

    /**
     * Get an optional Integer value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Integer.
     * @return An Optional containing the Integer value if found,
     * or an empty Optional if not present.
     */
    fun int(field: String) = string(field).map { s: String -> s.toInt() }

    /**
     * Get an optional array of Strings from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String array.
     * @return An Optional containing the array of Strings if found,
     * or an empty Optional if not present.
     */
    fun stringArray(field: String) = node(field).map { node: ParserContext ->
        val array: MutableList<String> = ArrayList()
        when {
            !node.node.isArray -> array.add(node.asText(field, node.node))
            else -> node.node.forEach(Consumer { array.add(asText(field, it)) })
        }
        array.toTypedArray()
    }

    /**
     * Get an optional map of String keys and ParserContext values from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional node map.
     * @return An Optional containing the map of String keys and ParserContext values if found,
     * or an empty Optional if not present.
     */
    fun nodeMap(field: String) = node(field).map { root: ParserContext ->
        val map: MutableMap<String, ParserContext> = HashMap()
        val fieldsIterator: Iterator<Map.Entry<String, JsonNode>> = root.node.fields()
        while (fieldsIterator.hasNext()) {
            val (key, value) = fieldsIterator.next()
            map[key] = ParserContext(value, computables, context, api)
        }
        map
    }

    /**
     * Parses only single functions, like: <on-click type="action">...</on-click>
     */
    fun functions(field: String) = node(field).map { FunctionProcessor.createFunctions(it) }

    /**
     * Parses single functions and function groups from the current node.
     * Example:
     *        <on-click type="random">
     *            <group>
     *              <function type="action">[player] teleport 0 0 0</function>
     *              <function type="action">[message] You teleported to the best spawn</function>
     *            </group>
     *             <function type="action">[player] teleport 10 10 10</function>
     *             <function type="action">[player] teleport -2000 -2000 -2000</function>
     *         </on-click>
     */
    fun groupableFunctions(
        functionsFieldName: String = "function", groupFieldName: String = "group"
    ): List<GuiFunction> {
        val functions = functions(functionsFieldName).map { it.toMutableList() }.optional(mutableListOf())
        functions.addAll(fieldList(groupFieldName).map { groups ->
            groups.map {
                it.functions(functionsFieldName)
                    .require { GuiIORuntimeException("Function groups aren't allowed to be empty") }
            }
        }.map { groups ->
            groups.map { functions ->
                GuiFunction.anonymous {
                    FunctionProcessor.callFunctions(functions, it).get()
                }
            }
        }.optional(emptyList()))
        return functions
    }

    protected open fun asText(field: String, node: JsonNode): String = node.asText().let { raw ->
        context?.let {
            val processed = FunctionProcessor.applyFunctions(it, raw)
            if (raw != processed) computables[field] = raw
            processed
        } ?: raw
    }

    override fun iterator(): Iterator<ParserContext> =
        node.map { ParserContext(it, computables, context, api) }.iterator()
}