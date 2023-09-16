package io.github.toberocat.guiengine.components.provided.toggle

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException

/**
 * Builder class for creating instances of [ToggleItemComponent].
 */
class ToggleItemComponentBuilder : AbstractGuiComponentBuilder<ToggleItemComponentBuilder>() {
    private var options: ParserContext? = null
    private var selected = 0

    /**
     * Set the options for the toggle item component.
     *
     * @param options The parser context containing the options for the toggle item.
     * @return This builder instance.
     */
    fun setOptions(options: ParserContext): ToggleItemComponentBuilder {
        this.options = options
        return this
    }

    /**
     * Set the index of the initially selected option.
     *
     * @param selected The index of the initially selected option.
     * @return This builder instance.
     */
    fun setSelected(selected: Int): ToggleItemComponentBuilder {
        this.selected = selected
        return this
    }

    /**
     * Create a new instance of [ToggleItemComponent] with the provided parameters.
     *
     * @return The created ToggleItemComponent instance.
     * @throws AssertionError if options are not set.
     */
    override fun createComponent(): ToggleItemComponent {
        assert(
            null != options // Ensure options are set before creating the component.
        )
        return ToggleItemComponent(
            x,
            y,
            1,
            1,
            priority,
            id,
            clickFunctions,
            dragFunctions,
            closeFunctions,
            hidden,
            options!!,
            selected
        )
    }

    /**
     * Deserialize the builder from a parser context node.
     *
     * @param node The parser context node containing the serialized data.
     * @throws IOException if there is an error while deserializing.
     */
    @Throws(IOException::class)
    override fun deserialize(node: ParserContext) {
        super.deserialize(node)
        setOptions(node.node("option").require(this))
        setSelected(node.int("selected").require(this))
    }
}
