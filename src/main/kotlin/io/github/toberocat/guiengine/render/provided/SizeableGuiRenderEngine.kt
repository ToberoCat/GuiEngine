package io.github.toberocat.guiengine.render.provided

import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.context.provided.SizeableGuiContext
import io.github.toberocat.guiengine.render.GuiRenderEngine
import io.github.toberocat.guiengine.utils.orElseThrow
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class SizeableGuiRenderEngine : GuiRenderEngine {
    override val guiViewManager = GuiEngineApiPlugin.plugin.getGuiViewManager()

    override fun createInventory(
        context: GuiContext,
        viewer: Player,
        placeholders: Map<String, String>
    ): Inventory? = (context as? SizeableGuiContext)
        ?.let { Bukkit.createInventory(viewer, 9 * it.height, it.title) }

    override fun width(context: GuiContext) = (context as? SizeableGuiContext)?.width
        .orElseThrow("Incompatible guicontext provided")

    override fun height(context: GuiContext) = (context as? SizeableGuiContext)?.height
        .orElseThrow("Incompatible guicontext provided")
}