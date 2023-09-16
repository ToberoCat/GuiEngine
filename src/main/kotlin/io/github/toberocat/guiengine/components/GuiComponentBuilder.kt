package io.github.toberocat.guiengine.components

import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException

/**
 * Created: 10.07.2023
 *
 *
 * This class should make it easier to create inheritable builder patterns.
 * It also tries to NOT merge the create and deserialize function, so you're forced to use variables, making it easier to
 * create a builder pattern (As intended for easier use without requiring an interpreter)
 *
 * @author Tobias Madlberger (Tobias)
 */
interface GuiComponentBuilder {
    /**
     * Create a new instance of the GUI component.
     *
     * @return A new instance of the GUI component.
     */
    fun createComponent(): GuiComponent

    /**
     * Deserialize the properties of the GUI component from the provided ParserContext node.
     * This method is used to set the properties of the component using the data from the ParserContext.
     *
     * @param node The ParserContext node containing the properties of the GUI component.
     * @throws IOException If there is an error while deserializing the properties.
     */
    @Throws(IOException::class)
    fun deserialize(node: ParserContext)
}
