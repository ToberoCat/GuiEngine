package io.github.toberocat.guiengine.context.provided

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.interpreter.provided.ChestInterpreter

class ChestGuiContext(interpreter: ChestInterpreter, val title: String) : GuiContext(interpreter)