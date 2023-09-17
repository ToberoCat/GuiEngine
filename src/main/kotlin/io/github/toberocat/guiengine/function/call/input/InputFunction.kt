package io.github.toberocat.guiengine.function.call.input

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.GuiFunctionException
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.guiengine.xml.parsing.PlaceholderParserContext
import io.github.toberocat.guiengine.xml.parsing.PlaceholderParserContext.Companion.toPlaceholderContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@JsonDeserialize(using = InputFunction.Deserializer::class)
data class InputFunction(val inputType: InputType, val variable: String, val parser: ParserContext) : GuiFunction {
    override val type = TYPE

    override fun call(context: GuiContext) {
        context.viewer()?.let { viewer ->
            Bukkit.getScheduler().runTask(GuiEngineApiPlugin.plugin, Runnable {
                inputType.takeInput(viewer, parser) {
                    runBody(
                        viewer, context, it
                    )
                }
            })
        }
    }

    private fun runBody(viewer: Player, context: GuiContext, input: String) {
        val placeholders: MutableMap<String, String> = when (parser) {
            is PlaceholderParserContext -> parser.placeholders
            else -> mutableMapOf()
        }
        placeholders["%$variable%"] = input
        val functions = parser.toPlaceholderContext(placeholders).functions("function")
            .require { GuiFunctionException("Input function must have at least a single function") }
        FunctionProcessor.callFunctions(functions, context)
        Bukkit.getScheduler().runTask(GuiEngineApiPlugin.plugin, Runnable {
            context.inventory()?.let { viewer.openInventory(it) }
            context.render()
        })
    }

    class Deserializer : GuiFunctionFactory<InputFunction>() {
        override fun build(node: ParserContext) = InputFunction(
            node.enum(InputType::class.java, "input-type").require(TYPE, javaClass),
            node.string("variable").require(TYPE, javaClass),
            node
        )
    }

    companion object {
        const val TYPE = "input"
    }
}