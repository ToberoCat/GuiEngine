package io.github.toberocat.guiengine.exception;

/**
 * Represents a runtime exception that occurs during GUI I/O operations in the GUI engine.
 * <p>
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiIORuntimeException extends RuntimeException {

    /**
     * Constructs a new `GuiIORuntimeException` with the specified error message.
     *
     * @param message The error message for the exception.
     */
    public GuiIORuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new `GuiIORuntimeException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GuiIORuntimeException(Throwable cause) {
        super(cause);
    }
}
