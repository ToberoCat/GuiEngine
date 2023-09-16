package io.github.toberocat.guiengine.exception

/**
 * Represents a runtime exception that occurs during GUI I/O operations in the GUI engine.
 *
 *
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
open class GuiIORuntimeException : GuiException {
    constructor(e: Throwable) : super(e)
    constructor(message: String) : super(message)
}
