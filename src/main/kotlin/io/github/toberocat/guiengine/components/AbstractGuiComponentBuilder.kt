package io.github.toberocat.guiengine.components

import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import java.io.IOException
import java.util.*

/**
 * An abstract base class for building GUI components.
 * This class provides common properties and methods for building GUI components.
 * Created: 10.07.2023
 * Author: Tobias Madlberger (Tobias)
 *
 * @param <B> The type of the builder class.
</B> */
abstract class AbstractGuiComponentBuilder<B : AbstractGuiComponentBuilder<B>> : GuiComponentBuilder {
    protected var priority = RenderPriority.NORMAL

    /**
     * Get the ID of the GUI component.
     *
     * @return The ID of the GUI component.
     */
    var id = UUID.randomUUID().toString()
        protected set

    protected var x = 0

    protected var y = 0

    protected var hidden = false

    protected var clickFunctions: List<GuiFunction> = ArrayList()
    protected var dragFunctions: List<GuiFunction> = ArrayList()
    protected var closeFunctions: List<GuiFunction> = ArrayList()

    /**
     * Set the rendering priority of the GUI component.
     *
     * @param priority The RenderPriority to set.
     * @return The builder instance.
     */
    fun setPriority(priority: RenderPriority): B {
        this.priority = priority
        return self()
    }

    /**
     * Set the ID of the GUI component.
     *
     * @param id The ID to set.
     * @return The builder instance.
     */
    fun setId(id: String): B {
        this.id = id
        return self()
    }

    /**
     * Set the X coordinate of the GUI component.
     *
     * @param x The X coordinate to set.
     * @return The builder instance.
     */
    fun setX(x: Int): B {
        this.x = x
        return self()
    }

    /**
     * Set the Y coordinate of the GUI component.
     *
     * @param y The Y coordinate to set.
     * @return The builder instance.
     */
    fun setY(y: Int): B {
        this.y = y
        return self()
    }

    /**
     * Set the visibility of the GUI component.
     *
     * @param hidden true to hide the component, false to make it visible.
     * @return The builder instance.
     */
    fun setHidden(hidden: Boolean): B {
        this.hidden = hidden
        return self()
    }

    /**
     * Set the click functions for the GUI component.
     *
     * @param clickFunctions The list of click functions to set.
     * @return The builder instance.
     */
    fun setClickFunctions(clickFunctions: List<GuiFunction>): B {
        this.clickFunctions = clickFunctions
        return self()
    }

    /**
     * Set the drag functions for the GUI component.
     *
     * @param dragFunctions The list of drag functions to set.
     * @return The builder instance.
     */
    fun setDragFunctions(dragFunctions: List<GuiFunction>): B {
        this.dragFunctions = dragFunctions
        return self()
    }

    /**
     * Set the close functions for the GUI component.
     *
     * @param closeFunctions The list of close functions to set.
     * @return The builder instance.
     */
    fun setCloseFunctions(closeFunctions: List<GuiFunction>): B {
        this.closeFunctions = closeFunctions
        return self()
    }

    /**
     * Deserialize the properties of the GUI component from the provided ParserContext node.
     *
     * @param node The ParserContext node containing the properties of the GUI component.
     * @throws IOException If there is an error while deserializing the properties.
     */
    @Throws(IOException::class)
    override fun deserialize(node: ParserContext) {
        setPriority(node.renderPriority("priority").optional(RenderPriority.NORMAL))
        setId(node.string("id").optional(id))
        setClickFunctions(node.functions("on-click").optional(ArrayList()))
        setDragFunctions(node.functions("on-drag").optional(ArrayList()))
        setCloseFunctions(node.functions("on-close").optional(ArrayList()))
        setX(node.int("x").optional(0))
        setY(node.int("y").optional(0))
        setHidden(node.boolean("hidden").optional(false))
    }

    /**
     * Helper method to return the builder instance.
     *
     * @return The builder instance.
     */
    protected fun self(): B {
        return this as B
    }
}
