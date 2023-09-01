package io.github.toberocat.guiengine.render

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.InvalidGuiFileException
import io.github.toberocat.guiengine.view.GuiView
import io.github.toberocat.guiengine.view.GuiViewManager
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * GuiRenderEngine is an interface for rendering GUIs and managing their views.
 *
 *
 * Created: 06.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiRenderEngine : GuiVirtualizer {
    /**
     * Retrieves the GuiViewManager associated with this render engine.
     *
     * @return The GuiViewManager instance.
     */
    val guiViewManager: GuiViewManager

    /**
     * Renders the GUI using the provided render buffer, GuiContext, and Player viewer.
     *
     * @param renderBuffer The 2D array of ItemStacks representing the render buffer.
     * @param context      The GuiContext to be rendered.
     * @param viewer       The Player who will view the rendered GUI.
     */
    fun renderGui(renderBuffer: Array<Array<ItemStack>>, context: GuiContext, viewer: Player) {
        context.componentsAscending().filter { !it.hidden() }
            .forEachOrdered { it.render(viewer, renderBuffer) }
    }


    /**
     * Shows the GUI to the specified player using the provided GuiContext and placeholders.
     *
     * @param content      The GuiContext representing the GUI content to be displayed.
     * @param viewer       The Player who will view the GUI.
     * @param placeholders A map of placeholders used for dynamic content in the GUI.
     */
    fun showGui(
        context: GuiContext, viewer: Player, placeholders: Map<String, String>
    ) {
        val inventory = createInventory(context, viewer, placeholders) ?: throw InvalidGuiFileException(
            "The gui renderer wasn't able to create a inventory. " + "Probably incompatible gui contexts"
        )
        context.componentsDescending().forEach { it?.onViewInit(placeholders) }
        context.render()
        guiViewManager.registerGui(viewer.uniqueId, GuiView(inventory, context))
        viewer.openInventory(inventory)
    }

    /**
     * Creates an inventory representing the GUI using the provided GuiContext and placeholders.
     *
     * @param context      The GuiContext representing the GUI content.
     * @param viewer       The Player who will view the GUI.
     * @param placeholders A map of placeholders used for dynamic content in the GUI.
     * @return The created Inventory representing the GUI.
     */
    fun createInventory(
        context: GuiContext, viewer: Player, placeholders: Map<String, String>
    ): Inventory?
}