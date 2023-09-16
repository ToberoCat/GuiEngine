package io.github.toberocat.guiengine.function

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.GuiFunctionException
import io.github.toberocat.guiengine.utils.Utils
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.util.*
import java.util.concurrent.Future
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Utility class for processing and handling GUI functions.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
object FunctionProcessor {
    private val pattern = Pattern.compile("\\{([^}]*)}", Pattern.MULTILINE)
    private val FUNCTIONS: MutableMap<String, Class<out GuiFunction>> = HashMap()
    private val COMPUTE_FUNCTIONS: MutableSet<ComputeFunction> = HashSet()
    private val OBJECT_MAPPER: ObjectMapper = XmlMapper().registerKotlinModule()

    /**
     * Registers a custom GUI function.
     *
     * @param id    The ID of the function.
     * @param clazz The class implementing the custom GUI function.
     */
    fun registerFunction(id: String, clazz: Class<out GuiFunction>) {
        FUNCTIONS[id] = clazz
    }

    /**
     * Registers a custom compute function.
     *
     * @param computeFunction The custom compute function to register.
     */
    fun registerComputeFunction(computeFunction: ComputeFunction) {
        COMPUTE_FUNCTIONS.add(computeFunction)
    }

    /**
     * Creates a GUI function from a JSON node.
     *
     * @param node The JSON node containing the function information.
     * @return The created `GuiFunction` instance, or null if the function could not be created.
     * @throws JsonProcessingException If there is an issue with processing the JSON node.
     */
    @Throws(JsonProcessingException::class)
    fun createFunction(node: JsonNode): GuiFunction {
        val id = ParserContext.empty(node)
            .string("type")
            .require { GuiFunctionException("A gui function is missing a type") }
        return OBJECT_MAPPER.treeToValue(node, FUNCTIONS[id])
    }

    fun createFunctions(node: JsonNode): List<GuiFunction> {
        try {
            val functions: MutableList<GuiFunction> = LinkedList()
            if (node.isArray) {
                for (function in node) functions.add(createFunction(function!!))
            } else {
                functions.add(createFunction(node))
            }
            return functions
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Calls a collection of GUI functions with the specified API and context.
     *
     * @param functions The collection of GUI functions to call.
     * @param context   The `GuiContext` instance representing the GUI context on which the functions are called.
     */
    fun callFunctions(
        functions: Collection<GuiFunction>,
        context: GuiContext
    ): Future<*> = Utils.threadPool.submit { for (function in functions) function.call(context) }

    /**
     * Applies to compute functions to the provided value, replacing placeholders with their computed values.
     *
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing placeholders to compute.
     * @return The computed value with placeholders replaced by their computed values.
     */
    fun applyFunctions(context: GuiContext, value: String): String {
        val buffer = StringBuilder()
        val matcher = pattern.matcher(value)
        while (matcher.find()) {
            val group = matcher.group(1)
            for (function in COMPUTE_FUNCTIONS) {
                if (!function.checkForFunction(group)) continue

                matcher.appendReplacement(buffer, Matcher.quoteReplacement(function.compute(context, group)))
                break
            }
        }
        matcher.appendTail(buffer)
        return buffer.toString()
    }
}
