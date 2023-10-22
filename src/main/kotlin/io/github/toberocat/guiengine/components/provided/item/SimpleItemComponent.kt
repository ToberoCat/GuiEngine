package io.github.toberocat.guiengine.components.provided.item

import com.fasterxml.jackson.databind.SerializerProvider
import io.github.toberocat.guiengine.components.AbstractGuiComponent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.utils.JsonUtils.writeArray
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.io.IOException

/**
 * A GUI component representing a simple item in the GUI.
 * This component displays a single item at a specific position in the GUI.
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
open class SimpleItemComponent(
    offsetX: Int,
    offsetY: Int,
    priority: RenderPriority,
    id: String,
    clickFunctions: List<GuiFunction>,
    dragFunctions: List<GuiFunction>,
    closeFunctions: List<GuiFunction>,
    protected val stack: ItemStack,
    hidden: Boolean
) : AbstractGuiComponent(offsetX, offsetY, 1, 1, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden) {
    override val type = TYPE

    @Throws(IOException::class)
    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        super.serialize(gen, serializers)

        gen.writeStringField("material", stack.type.name)
        val meta = stack.itemMeta ?: return

        gen.writeStringField("name", meta.displayName)
        meta.lore?.let { writeArray(gen, "lore", it.toTypedArray()) }

        (meta as? SkullMeta)?.let { skullMeta ->
            skullMeta.owningPlayer?.let {
                gen.writeStringField(
                    "head-owner", it.uniqueId.toString()
                )
            }
        }
    }

    override fun render(viewer: Player, inventory: Array<Array<ItemStack>>) {
        if (null == context || null == api) return
        inventory[offsetY][offsetX] = stack
    }

    companion object {
        const val TYPE = "item"
    }
}
