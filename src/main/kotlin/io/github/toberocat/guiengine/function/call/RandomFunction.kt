package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import kotlin.random.Random

@JsonDeserialize(using = RandomFunction.Deserializer::class)
data class RandomFunction(val random: Random, val functions: List<GuiFunction>) : GuiFunction {
    override val type = TYPE

    override fun call(context: GuiContext) {
        FunctionProcessor.callFunctions(listOf(functions.random(random)), context).get()
    }

    class Deserializer : GuiFunctionFactory<RandomFunction>() {
        override fun build(node: ParserContext) = RandomFunction(
            node.string("seed")
                .map { Random(it.hashCode()) }
                .optional(Random.Default),
            node.groupableFunctions()
        )

    }

    companion object {
        const val TYPE = "random"
    }
}