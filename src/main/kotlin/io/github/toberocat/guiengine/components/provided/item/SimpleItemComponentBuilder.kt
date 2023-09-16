package io.github.toberocat.guiengine.components.provided.item

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import io.github.toberocat.toberocore.util.ItemUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.util.*

/**
 * A builder class for creating SimpleItemComponent instances.
 * This builder provides methods for setting various properties of the SimpleItemComponent.
 *
 * @param <B> The type of the builder, used for method chaining.
</B> */
open class SimpleItemComponentBuilder<B : SimpleItemComponentBuilder<B>>
    : AbstractGuiComponentBuilder<B>() {
    protected var name = ""
    protected var material: Material? = null
    protected var lore = arrayOf<String>()
    protected var textureId: String? = null
    protected var owner: UUID? = null

    /**
     * Set the texture ID for the item (only applicable to PLAYER_HEAD or SKULL items).
     *
     * @param textureId The texture ID for the item.
     * @return The builder instance (for method chaining).
     */
    fun setTextureId(textureId: String?): B {
        this.textureId = textureId
        return self()
    }

    /**
     * Set the owner UUID for the item (only applicable to PLAYER_HEAD or SKULL items).
     *
     * @param owner The owner UUID for the item.
     * @return The builder instance (for method chaining).
     */
    fun setOwner(owner: UUID?): B {
        this.owner = owner
        return self()
    }

    /**
     * Set the name of the item.
     *
     * @param name The name of the item.
     * @return The builder instance (for method chaining).
     */
    fun setName(name: String): B {
        this.name = name
        return self()
    }

    /**
     * Set the material of the item (e.g., DIAMOND, GOLD_INGOT, etc.).
     *
     * @param material The material of the item.
     * @return The builder instance (for method chaining).
     */
    fun setMaterial(material: Material): B {
        this.material = material
        return self()
    }

    /**
     * Set the lore of the item.
     *
     * @param lore The lore of the item as an array of strings.
     * @return The builder instance (for method chaining).
     */
    fun setLore(lore: Array<String>): B {
        this.lore = lore
        return self()
    }

    protected val itemStack: ItemStack
        get() {
            if (null != material) return ItemUtils.createItem(material!!, name, 1, *lore)
            return if (textureId != null) ItemUtils.createHead(
                textureId!!,
                name,
                1,
                *lore
            ) else if (owner != null) ItemUtils.createSkull(
                Bukkit.getOfflinePlayer(
                    owner!!
                ), 1, name, lore
            ) else ItemUtils.createItem(
                Material.BARRIER,
                "§cInvalid texture / ownerID",
                1
            )
        }

    override fun createComponent(): SimpleItemComponent {
        return SimpleItemComponent(x, y, priority, id, clickFunctions, dragFunctions, closeFunctions, itemStack, hidden)
    }

    @Throws(IOException::class)
    override fun deserialize(node: ParserContext) {
        super.deserialize(node)
        setName(node.string("name").optional(" "))
        setLore(node.stringArray("lore").optional(emptyArray()))
        val texture = node.string("head-texture")
            .map { obj: String -> obj.trim { it <= ' ' } }
            .nullable(null)
        val headOwner = node.uuid("head-owner")
            .nullable(null)
        if (null != texture || null != headOwner) {
            setOwner(headOwner)
            setTextureId(texture)
        } else {
            setMaterial(node.material("material").require(this))
        }
    }
}