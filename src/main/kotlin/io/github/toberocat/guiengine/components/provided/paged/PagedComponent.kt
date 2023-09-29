package io.github.toberocat.guiengine.components.provided.paged

import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.IntNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.action.NextPageAction
import io.github.toberocat.guiengine.action.PreviousPageAction
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.components.container.tab.Page
import io.github.toberocat.guiengine.components.container.tab.PagedContainer
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.utils.JsonUtils.writeArray
import io.github.toberocat.guiengine.utils.Utils.translateFromSlot
import io.github.toberocat.guiengine.xml.XmlGui
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.toberocore.action.Action
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.max
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
open class PagedComponent(
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
    private val parent: ParserContext,
    private val pattern: IntArray,
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
), PagedContainer {
    private var pages: MutableList<PatternPage> = mutableListOf()
    private var emptyFill: GuiComponent? = null
    private var pageParser: PageParser? = null

    override val type = TYPE

    override val availablePages: Int
        get() = pages.size

    override var page: Int
        get() = showedPage
        set(value) {
            require(!(0 > value || value >= pages.size)) { "Page not in valid bounds" }
            showedPage = value
            updateEmbedded()
            context?.render()
        }

    override fun addPage(context: GuiContext) = addPage(context, pages.size - 1)

    override fun addPage(context: GuiContext, position: Int) {
        api?.let { pages.add(position, PatternPage(it, pattern, context)) }
    }

    override fun createEmptyPage() = pageParser?.createEmptyPage()

    override fun addComponent(component: GuiComponent) {
        if (pages.last().insert(component)) return
        createEmptyPage()?.let { pages.add(it) }
    }

    override fun clearContainer() {
        pages.clear()
        createEmptyPage()?.let { pages.add(it) }
    }


    override fun onViewInit(placeholders: Map<String, String>) {
        pageParser = createParser()
        pages = pageParser?.parse(parent) ?: pages
        emptyFill = parent.node("empty-fill").map {
            context!!.interpreter()
                .createComponent(context!!.interpreter().xmlComponent(it.node, api!!), api!!, context!!)
        }.nullable(null)
        updateEmbedded()
    }

    override fun render(viewer: Player, inventory: Array<Array<ItemStack>>) {
        renderEmptyFill(viewer, inventory)
        super.render(viewer, inventory)
    }

    override fun addActions(actions: MutableSet<Action>) {
        actions.add(NextPageAction(this))
        actions.add(PreviousPageAction(this))
    }

    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        super.serialize(gen, serializers)
        writeArray(gen, "pattern", pattern)
        gen.writeNumberField("showing-page", showedPage)
        gen.writePOJOField("page", pages.map { it.pageContext.components })
    }

    private fun updateEmbedded() {
        embedded = pages[min(showedPage.toDouble(), (pages.size - 1).toDouble()).toInt()].pageContext
    }

    private fun createParser() = api?.let { api ->
        context?.let { context ->
            context.viewer()?.let {
                PageParser(
                    api, context, it, width(), height(), pattern
                )
            }
        }
    }

    private fun renderEmptyFill(viewer: Player, buffer: Array<Array<ItemStack>>) {
        emptyFill?.let {
            for (slot in pattern) {
                val (x, y) = translateFromSlot(slot)
                it.setX(offsetX + x)
                it.setY(offsetY + y)
                it.render(viewer, buffer)
            }
        }
    }

    companion object {
        const val TYPE = "paged"
    }
}

class PageParser(
    private val api: GuiEngineApi,
    private val context: GuiContext,
    private val viewer: Player,
    private val width: Int,
    private val height: Int,
    private val pattern: IntArray
) {
    fun parse(node: ParserContext): MutableList<PatternPage> {
        val pages = mutableListOf<PatternPage>()
        node.fieldList("page").optional(emptyList()).forEach {
            val (page, preferredPosition) = parsePage(it)
            pages.add(preferredPosition ?: (max(pages.size - 1, 0)), page)
        }
        if (pages.isEmpty()) pages.add(createEmptyPage())

        parseNode(node).forEach { (_, component) ->
            if (pages.last().insert(component)) return@forEach
            pages.add(createEmptyPage())
        }
        return pages
    }

    fun createEmptyPage(): PatternPage = PatternPage(api, pattern, context.interpreter().createContext(
        api, viewer, XmlGui(
            context.interpreter().interpreterId, emptyArray(), mapOf(
                "width" to IntNode(width), "height" to IntNode(height)
            )
        )
    ).also {
        it.setInventory(context.inventory())
        it.setViewer(viewer)
        it.domEvents.onRender.addAll(context.domEvents.onRender)
        it.computableFunctionProcessor.copy(context.computableFunctionProcessor)
    })

    private fun parseNode(node: ParserContext): List<Pair<Boolean, GuiComponent>> {
        val interpreter = context.interpreter()
        return node.fieldList("component").optional(ArrayList()).map {
            return@map (it.node("x").present() || it.node("y").present()) to interpreter.createComponent(
                interpreter.xmlComponent(it.node, api), api, context
            )
        }
    }

    private fun parsePage(node: ParserContext): Pair<PatternPage, Int?> = createEmptyPage().also { page ->
        parseNode(node).forEach { (positioned, component) ->
            if (positioned) page.pageContext.add(component) else page.insert(
                component
            )
        }
    } to node.int("position").nullable(null)
}

class PatternPage(
    val api: GuiEngineApi, private val pattern: IntArray, override val pageContext: GuiContext
) : Page {
    private var current: Int = 0
    override fun insert(component: GuiComponent): Boolean {
        if (current >= pattern.size) return false

        val slot = pattern[current++]
        val (x, y) = translateFromSlot(slot)
        component.setX(x)
        component.setY(y)
        pageContext.add(component)
        return true
    }

    override fun toString(): String {
        return "PatternPage(pageContext=$pageContext, current=$current)"
    }


}