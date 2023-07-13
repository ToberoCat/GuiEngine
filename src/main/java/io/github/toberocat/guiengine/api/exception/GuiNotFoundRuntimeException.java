package io.github.toberocat.guiengine.api.exception;

/**
 * Created: 05/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiNotFoundRuntimeException extends RuntimeException {
    public GuiNotFoundRuntimeException(String gui) {
        super("You tried to access gui " + gui + " which wasn't found in the specified gui folder");
    }
}
