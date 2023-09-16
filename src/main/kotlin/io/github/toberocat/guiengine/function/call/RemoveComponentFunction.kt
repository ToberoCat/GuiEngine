package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext

/**
 * Custom GUI function to remove a component from the GUI.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = RemoveComponentFunction.Deserializer::class)
data class RemoveComponentFunction(val target: String) : GuiFunction {
    override val type = TYPE

    /**
     * Calls the `removeById` method using the provided API and context to remove a component from the GUI.
     *
     * @param context The `GuiContext` instance representing the GUI context from which to remove the component.
     */
    override fun call(context: GuiContext) {
        context.removeById(target)
    }

    /**
     * Custom deserializer to convert JSON data into a `RemoveComponentFunction` instance.
     */
    class Deserializer : GuiFunctionFactory<RemoveComponentFunction>() {
        override fun build(node: ParserContext): RemoveComponentFunction =
            RemoveComponentFunction(node.string("target").require(TYPE, javaClass))
    }

    companion object {
        const val TYPE = "remove"
    }
}
