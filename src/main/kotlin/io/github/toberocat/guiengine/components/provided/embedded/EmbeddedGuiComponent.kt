package io.github.toberocat.guiengine.components.provided.embedded

import com.fasterxml.jackson.databind.SerializerProvider
import io.github.toberocat.guiengine.components.AbstractGuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.utils.Utils.translateToSlot
import io.github.toberocat.guiengine.utils.VirtualInventory
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import java.io.IOException
import kotlin.math.min

/**
 * A GUI component that embeds another GUI inside it.
 * This component allows embedding and rendering a target GUI inside the current GUI.
 * Created: 06.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
open class EmbeddedGuiComponent(
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
    protected val targetGui: String,
    protected val copyAir: Boolean,
    protected val interactions: Boolean
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
) {
    override val type = TYPE
    protected var embedded: GuiContext? = null

    @Throws(IOException::class)
    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        super.serialize(gen, serializers)
        gen.writeStringField("target-gui", targetGui)
        gen.writeBooleanField("copy-air", copyAir)
        gen.writeBooleanField("interactions", interactions)
    }

    override fun onViewInit(placeholders: Map<String, String>) {
        if (null == context || null == api || null == context!!.viewer()) return
        embedded =
            context!!.interpreter().loadContent(api!!, context!!.viewer()!!, api!!.loadXmlGui(placeholders, targetGui))
        embedded!!.setInventory(VirtualInventory(height) { context!!.render() })
        embedded!!.setViewer(context!!.viewer())
    }

    override fun clickedComponent(event: InventoryClickEvent) {
        super.clickedComponent(event)
        if (!interactions || null == embedded) return
        val fakedEvent = InventoryClickEvent(
            event.view,
            event.slotType,
            event.slot - translateToSlot(offsetX, offsetY),
            event.click,
            event.action,
            event.hotbarButton
        )
        embedded!!.clickedComponent(fakedEvent)
    }

    override fun draggedComponent(event: InventoryDragEvent) {
        super.draggedComponent(event)
        if (!interactions || null == embedded) return

        // ToDo: Fake the event (offset the slot to be originated at zero)
        embedded!!.draggedComponent(event)
    }

    override fun closedComponent(event: InventoryCloseEvent) {
        super.closedComponent(event)
        if (!interactions || null == embedded) return
        embedded!!.closedComponent(event)
    }

    override fun render(viewer: Player, inventory: Array<Array<ItemStack>>) {
        val renderEngine = (context ?: return)
            .interpreter()
            .renderEngine
        val virtualInventory = renderEngine.createBuffer(context ?: return)
        renderEngine.renderGui(virtualInventory, embedded ?: return, viewer)

        val definedWidth = renderEngine.width(context ?: return)
        val definedHeight = renderEngine.height(context ?: return)
        val width = min(definedWidth - offsetX, definedWidth)
        val height = min(definedHeight - offsetY, definedHeight)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val item = virtualInventory[y][x]
                if (copyAir || Material.AIR != item.type)
                    inventory[y + offsetY][x + offsetX] = item
            }
        }
    }

    companion object {
        const val TYPE = "embedded"
    }
}
