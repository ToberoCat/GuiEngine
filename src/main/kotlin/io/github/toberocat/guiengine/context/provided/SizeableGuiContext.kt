package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.ContextType
import io.github.toberocat.guiengine.interpreter.GuiInterpreter
import io.github.toberocat.guiengine.xml.XmlGui

class SizeableGuiContext(
    interpreter: GuiInterpreter,
    xmlGui: XmlGui,
    api: GuiEngineApi,
    title: String,
    val width: Int,
    val height: Int, contextType: ContextType
) : TitleContext(interpreter, xmlGui, api, title, contextType)