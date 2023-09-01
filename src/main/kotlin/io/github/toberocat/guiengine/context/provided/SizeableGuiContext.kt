package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.interpreter.GuiInterpreter

class SizeableGuiContext(
    interpreter: GuiInterpreter,
    title: String,
    val width: Int,
    val height: Int
) : TitleContext(interpreter, title)