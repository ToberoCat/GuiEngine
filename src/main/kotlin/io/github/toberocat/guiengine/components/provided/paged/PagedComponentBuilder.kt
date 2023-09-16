package io.github.toberocat.guiengine.components.provided.paged

import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponentBuilder
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException
import java.util.*

/**
 * A builder class for creating PagedComponent instances.
 *
 *
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class PagedComponentBuilder : EmbeddedGuiComponentBuilder<PagedComponentBuilder>() {
    private var showingPage = 0
    private var pattern = IntArray(0)
    private var parent: ParserContext? = null

    /**
     * Sets the pattern of slots on the GUI to arrange the components.
     *
     * @param pattern The pattern of slots as an integer array.
     * @return The builder instance.
     */
    fun setPattern(pattern: IntArray): PagedComponentBuilder {
        this.pattern = pattern
        return this
    }

    /**
     * Sets the parent parser context containing the GUI definition.
     *
     * @param parent The parent parser context.
     * @return The builder instance.
     */
    fun setParent(parent: ParserContext): PagedComponentBuilder {
        this.parent = parent
        return this
    }

    /**
     * Sets the index of the currently showing page.
     *
     * @param showingPage The index of the currently showing page.
     * @return The builder instance.
     */
    fun setShowingPage(showingPage: Int): PagedComponentBuilder {
        this.showingPage = showingPage
        return this
    }

    override fun createComponent(): PagedComponent {
        assert(null != parent)
        assert(null != targetGui)
        return PagedComponent(
            x,
            y,
            width,
            height,
            priority,
            id,
            clickFunctions,
            dragFunctions,
            closeFunctions,
            hidden,
            targetGui!!,
            copyAir,
            interactions,
            parent!!,
            pattern,
            showingPage
        )
    }

    @Throws(IOException::class)
    override fun deserialize(node: ParserContext) {
        deserialize(node, false)
        setShowingPage(node.int("showing-page").optional(0))
        setParent(node)
        setPattern(node.string("pattern").map { x: String ->
            Arrays.stream(x.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .mapToInt { s: String -> s.toInt() }.toArray()
        }.optional(IntArray(0)))
    }
}