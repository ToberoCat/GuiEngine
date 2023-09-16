package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.ContextType
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.interpreter.GuiInterpreter
import io.github.toberocat.guiengine.xml.XmlGui

open class TitleContext(
    interpreter: GuiInterpreter,
    xmlGui: XmlGui,
    api: GuiEngineApi,
    val title: String, contextType: ContextType
) : GuiContext(interpreter, api, xmlGui, contextType)