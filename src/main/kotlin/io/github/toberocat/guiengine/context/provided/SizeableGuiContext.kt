package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.interpreter.GuiInterpreter

class SizeableGuiContext(
    interpreter: GuiInterpreter,
    val title: String,
    val width: Int,
    val height: Int
) : GuiContext(interpreter)