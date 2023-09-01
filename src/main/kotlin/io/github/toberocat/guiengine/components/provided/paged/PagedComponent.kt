package io.github.toberocat.guiengine.components.provided.paged

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.IntNode
import io.github.toberocat.guiengine.action.NextPageAction
import io.github.toberocat.guiengine.action.PreviousPageAction
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.components.container.ContextContainer
import io.github.toberocat.guiengine.components.container.GuiComponentContainer
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.utils.GeneratorContext
import io.github.toberocat.guiengine.utils.JsonUtils.getOptionalNode
import io.github.toberocat.guiengine.utils.JsonUtils.writeArray
import io.github.toberocat.guiengine.utils.ParserContext
import io.github.toberocat.guiengine.utils.Utils.translateFromSlot
import io.github.toberocat.guiengine.utils.orElseThrow
import io.github.toberocat.guiengine.xml.XmlGui
import io.github.toberocat.toberocore.action.Action
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.util.function.Consumer
import kotlin.math.min

/**
 * A custom GUI component that represents a paged GUI.
 * It allows displaying multiple pages of components in a specific pattern on a GUI.
 * This component can be navigated using NextPageAction and PreviousPageAction actions.
 *
 *
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
// ToDo: Clean up paged component
class PagedComponent(
    offsetX: Int,
    offsetY: Int,
    width: Int,
    height: Int,
    priority: RenderPriority,
    id: String,
    clickFunctions: List<GuiFunction>,
    dragFunctions: List<GuiFunction>,
    closeFunctions: List<GuiFunction>,
    hidden: Boolean,
    targetGui: String,
    copyAir: Boolean,
    interactions: Boolean,
    protected val parent: ParserContext,
    protected val pattern: IntArray,
    protected var showedPage: Int
) : EmbeddedGuiComponent(
    offsetX,
    offsetY,
    width,
    height,
    priority,
    id,
    clickFunctions,
    dragFunctions,
    closeFunctions,
    hidden,
    targetGui,
    copyAir,
    interactions
), GuiComponentContainer, ContextContainer {
    private val pages: MutableList<GuiContext> = mutableListOf()
    private var emptyFill: GuiComponent? = null
    private var currentPatternIndex = 0

    override val type = TYPE

    @Throws(IOException::class)
    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        super.serialize(gen, serializers)
        writeArray(gen, "pattern", pattern)
        gen.writeNumberField("showing-page", showedPage)
        gen.writeRaw(parent.node.toString())
    }

    override fun onViewInit(placeholders: Map<String, String>) {
        addEmptyPage()
        try {
            parseComponents(parent) { this.addComponent(it) }
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        val pages = parent.getOptionalFieldList("page").orElse(ArrayList())
        for (page in pages) createPage(page)

        assert(null != context)
        assert(null != api)
        emptyFill = getOptionalNode(parent, "fill-empty").map { x: ParserContext ->
            try {
                return@map context!!.interpreter()
                    .createComponent(context!!.interpreter().xmlComponent(x.node, api!!), api!!, context!!)
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
        }.orElse(null)
        embedded = this.pages[showedPage]
    }

    /**
     * Adds a new page to the paged component.
     *
     * @param page The new page to add.
     */
    fun addPage(page: GuiContext) {
        pages.add(page)
    }

    /**
     * Adds a new page to the paged component at the specified position.
     *
     * @param page     The new page to add.
     * @param position The position at which to add the page.
     */
    fun addPage(page: GuiContext, position: Int) = pages.add(position, page)

    /**
     * Adds a new component to the current page.
     *
     * @param component The component to add.
     */
    override fun addComponent(component: GuiComponent) {
        assert(null != api)
        if (currentPatternIndex >= pattern.size) {
            currentPatternIndex = 0
            createEmptyPage()?.let { addPage(it) }
        }
        val page = pages[pages.size - 1]
        val slot = pattern[currentPatternIndex]
        currentPatternIndex++
        val (x, y) = translateFromSlot(slot)
        component.setX(x)
        component.setY(y)
        page.add(api!!, component)
        embedded = pages[min(showedPage.toDouble(), (pages.size - 1).toDouble()).toInt()]
    }

    override fun clearContainer() {
        pages.clear()
        resetPatternOnPage()
        addEmptyPage()
    }

    override fun addContext(context: GuiContext) = addPage(context)

    /**
     * Creates an empty page for the paged component.
     *
     * @return The created empty page.
     */
    private fun createEmptyPage(): GuiContext? {
        if (context == null || api == null || context!!.viewer() == null)
            return null
        val context = context!!
        val page = context.interpreter().createContext(
            api!!, context.viewer()!!, XmlGui(
                context.interpreter().interpreterId,
                emptyArray(),
                mapOf(
                    "width" to IntNode(width()),
                    "height" to IntNode(height())
                )
            )
        )
        page.setInventory(context.inventory())
        page.setViewer(context.viewer())
        return page
    }

    override fun render(viewer: Player, inventory: Array<Array<ItemStack>>) {
        if (null != emptyFill) {
            for (slot in pattern) {
                val (x, y) = translateFromSlot(slot)
                emptyFill!!.setX(offsetX + x)
                emptyFill!!.setY(offsetY + y)
                emptyFill!!.render(viewer, inventory)
            }
        }
        super.render(viewer, inventory)
    }

    override fun addActions(actions: MutableSet<Action>) {
        if (null == context || null == api) return
        actions.add(NextPageAction(this))
        actions.add(PreviousPageAction(this))
    }

    /**
     * Sets the currently showing page index of the paged component.
     *
     * @param page The index of the page to show.
     * @throws IllegalArgumentException If the specified page is not within the valid bounds.
     */
    fun setShowingPage(page: Int) {
        require(!(0 > page || page >= pages.size)) { "Page not in valid bounds" }
        showedPage = page
        embedded = pages[showedPage]
        if (null == context) return
        context!!.render()
    }

    /**
     * Gets the index of the currently showing page of the paged component.
     *
     * @return The index of the currently showing page.
     */
    fun getShowingPage(): Int {
        return showedPage
    }

    val page: Int
        /**
         * Gets the current page number of the paged component.
         *
         * @return The current page number (1-based index).
         */
        get() = showedPage + 1
    val availablePages: Int
        /**
         * Gets the total number of available pages in the paged component.
         *
         * @return The total number of available pages.
         */
        get() = pages.size

    /**
     * Creates a new empty page
     */
    fun addEmptyPage() {
        createEmptyPage()?.let { addPage(it) }
    }

    /**
     * Resets the pattern. This will break stuff with the automated paging if used wrong
     */
    fun resetPatternOnPage() {
        currentPatternIndex = 0
    }

    /**
     * Parses the components from the parent parser context and adds them to the specified consumer.
     *
     * @param parent    The parent parser context containing the components.
     * @param addToPage The consumer function to add the components to the page.
     * @throws JsonProcessingException If there is an error while processing the JSON data.
     */
    @Throws(JsonProcessingException::class)
    private fun parseComponents(parent: ParserContext, addToPage: Consumer<GuiComponent>) {
        assert(null != context)
        assert(null != api)
        val components = parent.getOptionalFieldList("component").orElse(ArrayList())
        for (component in components) {
            val xml = context!!.interpreter().xmlComponent(component.node, api!!)
            val guiComponent = context!!.interpreter().createComponent(xml, api!!, context!!)
            addToPage.accept(guiComponent)
        }
    }

    /**
     * Creates a new page for the paged component based on the specified parser context.
     *
     * @param pageNode The parser context representing the page data.
     */
    private fun createPage(pageNode: ParserContext) {
        assert(null != api)
        val position = pageNode.getOptionalInt("position").orElse(pages.size - 1)
        val page = createEmptyPage().orElseThrow("No gui component got created")
        try {
            parseComponents(pageNode) { component: GuiComponent? ->
                page.add(
                    api!!, component!!
                )
            }
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        addPage(page, position)
    }

    companion object {
        const val TYPE = "paged"
    }
}
