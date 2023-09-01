package io.github.toberocat.guiengine.exception

/**
 * Created: 05.08.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class InvalidGuiFileException
/**
 * Constructs a new runtime exception with the specified detail message.
 * The cause is not initialized, and may subsequently be initialized by a
 * call to [.initCause].
 *
 * @param message the detail message. The detail message is saved for
 * later retrieval by the [.getMessage] method.
 */
    (message: String?) : RuntimeException(message)
