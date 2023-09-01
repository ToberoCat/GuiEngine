package io.github.toberocat.guiengine.exception

/**
 * Represents a runtime exception that occurs during GUI I/O operations in the GUI engine.
 *
 *
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiIORuntimeException : RuntimeException {
    /**
     * Constructs a new `GuiIORuntimeException` with the specified error message.
     *
     * @param message The error message for the exception.
     */
    constructor(message: String?) : super(message)

    /**
     * Constructs a new `GuiIORuntimeException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    constructor(cause: Throwable?) : super(cause)
}
