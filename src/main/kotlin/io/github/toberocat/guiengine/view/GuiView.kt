package io.github.toberocat.guiengine.view

import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.inventory.Inventory

/**
 * A record class representing a GUI view in a Minecraft plugin.
 */
data class GuiView(val inventory: Inventory, val context: GuiContext)
