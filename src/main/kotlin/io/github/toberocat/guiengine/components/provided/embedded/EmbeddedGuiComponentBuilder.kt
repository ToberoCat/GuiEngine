package io.github.toberocat.guiengine.components.provided.embedded

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.utils.ParserContext
import io.github.toberocat.guiengine.utils.orElseThrow
import java.io.IOException

/**
 * A builder class for creating EmbeddedGuiComponent instances.
 * This builder provides methods for setting various properties of the EmbeddedGuiComponent.
 *
 * @param <B> The type of the builder, used for method chaining.
</B> */
open class EmbeddedGuiComponentBuilder<B : EmbeddedGuiComponentBuilder<B>> : AbstractGuiComponentBuilder<B>() {
    protected var width = 1
    protected var height = 1
    protected var targetGui: String? = null
    protected var copyAir = true
    protected var interactions = true

    /**
     * Set the width of the EmbeddedGuiComponent.
     *
     * @param width The width of the component.
     * @return The builder instance (for method chaining).
     */
    fun setWidth(width: Int): B {
        this.width = width
        return self()
    }

    /**
     * Set the height of the EmbeddedGuiComponent.
     *
     * @param height The height of the component.
     * @return The builder instance (for method chaining).
     */
    fun setHeight(height: Int): B {
        this.height = height
        return self()
    }

    /**
     * Set the ID of the target GUI to embed inside this component.
     *
     * @param targetGui The ID of the target GUI to embed.
     * @return The builder instance (for method chaining).
     */
    fun setTargetGui(targetGui: String): B {
        this.targetGui = targetGui
        return self()
    }

    /**
     * Set whether to copy air slots from the embedded GUI or skip empty slots.
     *
     * @param copyAir true to copy air slots, false to skip empty slots.
     * @return The builder instance (for method chaining).
     */
    fun setCopyAir(copyAir: Boolean): B {
        this.copyAir = copyAir
        return self()
    }

    /**
     * Set whether to allow interactions with the embedded GUI or ignore interactions.
     *
     * @param interactions true to allow interactions, false to ignore interactions.
     * @return The builder instance (for method chaining).
     */
    fun setInteractions(interactions: Boolean): B {
        this.interactions = interactions
        return self()
    }

    override fun createComponent(): EmbeddedGuiComponent {
        assert(null != targetGui)
        return EmbeddedGuiComponent(
            x,
            y,
            width,
            height,
            priority,
            id,
            clickFunctions,
            dragFunctions,
            closeFunctions,
            hidden,
            targetGui!!,
            copyAir,
            interactions
        )
    }

    @Throws(IOException::class)
    override fun deserialize(node: ParserContext) {
        deserialize(node, true)
    }

    @Throws(IOException::class)
    protected fun deserialize(node: ParserContext, forceTarget: Boolean) {
        super.deserialize(node)
        setCopyAir(node.getOptionalBoolean("copy-air").orElse(true))
        setInteractions(node.getOptionalBoolean("interactions").orElse(true))
        setWidth(node.getOptionalInt("width").orElseThrow(this, "width"))
        setHeight(node.getOptionalInt("height").orElseThrow(this, "height"))
        setTargetGui(node.getOptionalString("target-gui").let {
            when {
                !forceTarget -> it.orElse("")
                else -> it.orElseThrow(this, "target-gui")
            }
        })
    }
}
