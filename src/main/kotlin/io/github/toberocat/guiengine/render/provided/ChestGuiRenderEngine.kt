package io.github.toberocat.guiengine.render.provided

import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.context.provided.ChestGuiContext
import io.github.toberocat.guiengine.render.GuiRenderEngine
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

/**
 * DefaultGuiRenderEngine is an implementation of the GuiRenderEngine interface responsible for rendering GUIs.
 *
 *
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
class ChestGuiRenderEngine : GuiRenderEngine {
    override val guiViewManager = GuiEngineApiPlugin.plugin.getGuiViewManager()
    override fun createInventory(
        context: GuiContext, viewer: Player,
        placeholders: Map<String, String>
    ): Inventory? = (context as? ChestGuiContext)
        ?.let { Bukkit.createInventory(viewer, InventoryType.CHEST, it.title) }

    override fun width(context: GuiContext) = 9
    override fun height(context: GuiContext) = 3
}