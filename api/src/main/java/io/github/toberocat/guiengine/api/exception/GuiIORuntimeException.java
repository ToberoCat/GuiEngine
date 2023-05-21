package io.github.toberocat.guiengine.api.exception;

/**
 * Created: 05/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiIORuntimeException extends RuntimeException {
    public GuiIORuntimeException(String message) {
        super(message);
    }

    public GuiIORuntimeException(Throwable cause) {
        super(cause);
    }
}
