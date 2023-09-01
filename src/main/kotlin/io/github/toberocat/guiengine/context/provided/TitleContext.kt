package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.interpreter.GuiInterpreter

open class TitleContext(interpreter: GuiInterpreter, val title: String) : GuiContext(interpreter)