package io.github.toberocat.guiengine.components.provided.toggle

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.toberocat.guiengine.components.AbstractGuiComponent
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.components.container.Selectable
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.util.*

/**
 * Represents a toggle item component in a GUI. A toggle item can have multiple options, and clicking on the component
 * cycles through these options.
 *
 *
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class ToggleItemComponent(
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
    val options: ParserContext,
    override var selected: Int
) : AbstractGuiComponent(
    offsetX,
    offsetY,
    width,
    height,
    priority,
    id,
    clickFunctions,
    dragFunctions,
    closeFunctions,
    hidden
), Selectable {
    private var componentSelectionModel: Array<GuiComponent> = emptyArray()
    override var selectionModel: Array<String> = emptyArray()

    override val type = TYPE

    /**
     * Get the index of the currently selected option.
     *
     * @return The index of the selected option.
     */

    @Throws(IOException::class)
    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        super.serialize(gen, serializers)
        gen.writePOJOField("option", options)
        gen.writeNumberField("selected", selected)
    }

    override fun onViewInit(placeholders: Map<String, String>) {
        componentSelectionModel = createSelectionModel()
    }

    /**
     * Creates the selection model for the toggle item from the provided options in the parser context.
     *
     * @return The selection model as an array of GuiComponents.
     */
    private fun createSelectionModel(): Array<GuiComponent> {
        if (null == context || null == api)
            return emptyArray()

        val nodes = options.fieldList()
        val components: MutableList<GuiComponent> = ArrayList()
        val selectionModel: MutableList<String> = ArrayList()

        try {
            for (node in nodes) {
                selectionModel.add(node.string("value").require(id, javaClass))
                val xmlComponent = context!!.interpreter().xmlComponent(node.node, api!!)
                val component = context!!.interpreter().createComponent(xmlComponent, api!!, context!!)
                components.add(component)
            }
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        this.selectionModel = selectionModel.toTypedArray()
        return components.toTypedArray()
    }

    override fun clickedComponent(event: InventoryClickEvent) {
        super<AbstractGuiComponent>.clickedComponent(event)
        selected = (selected + 1) % componentSelectionModel.size
        if (null == context) return
        context!!.render()
    }

    override fun render(viewer: Player, inventory: Array<Array<ItemStack>>) {
        val virtualInventory = (context ?: return)
            .interpreter()
            .renderEngine
            .createBuffer(context ?: return)

        componentSelectionModel.get(selected).render(viewer, virtualInventory)
        inventory[offsetY][offsetX] = virtualInventory[0][0]
    }

    companion object {
        const val TYPE = "toggle"
    }
}
