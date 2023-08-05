package io.github.toberocat.guiengine.exception;

/**
 * Created: 05.08.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class InvalidGuiFileException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidGuiFileException(String message) {
        super(message);
    }
}
