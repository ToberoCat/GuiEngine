package io.github.toberocat.guiengine.event

import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.xml.XmlGui
import io.github.toberocat.guiengine.xml.parsing.ParserContext

class GuiDomEvents(private val xmlGui: XmlGui) {
    val onLoad: MutableList<GuiFunction>
    val onRender: MutableList<GuiFunction>

    init {
        onRender = loadFunction("on-render")
        onLoad = loadFunction("on-load")
    }

    private fun loadFunction(fieldName: String) =
        xmlGui[fieldName].map { FunctionProcessor.createFunctions(ParserContext.empty(it)) }
            .orElse(emptyList())
            .toMutableList()
}