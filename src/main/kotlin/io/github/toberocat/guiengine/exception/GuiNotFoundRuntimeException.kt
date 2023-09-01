package io.github.toberocat.guiengine.exception

/**
 * Represents a runtime exception that occurs when a GUI is not found in the specified GUI folder.
 *
 *
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiNotFoundRuntimeException
/**
 * Constructs a new `GuiNotFoundRuntimeException` with the specified GUI ID.
 *
 * @param gui The ID of the GUI that wasn't found in the specified GUI folder.
 */
    (gui: String) : RuntimeException("You tried to access gui $gui which wasn't found in the specified gui folder")
