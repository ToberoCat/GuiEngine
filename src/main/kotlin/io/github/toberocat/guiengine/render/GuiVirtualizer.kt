package io.github.toberocat.guiengine.render

import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface GuiVirtualizer {
    fun width(context: GuiContext): Int
    fun height(context: GuiContext): Int

    fun createBuffer(context: GuiContext): Array<Array<ItemStack>> = Array(height(context)) {
        Array(width(context)) { ItemStack(Material.AIR) }
    }
}