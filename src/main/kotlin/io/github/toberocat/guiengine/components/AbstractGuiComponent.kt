package io.github.toberocat.guiengine.components

import com.fasterxml.jackson.databind.SerializerProvider
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.FunctionProcessor.callFunctions
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.guiengine.xml.parsing.GeneratorContext
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import java.io.IOException

/**
 * An abstract base class for GUI components.
 * This class provides common functionality for GUI components and implements the GuiComponent interface.
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
abstract class AbstractGuiComponent protected constructor(
    protected var offsetX: Int,
    protected var offsetY: Int,
    private val width: Int,
    protected val height: Int,
    private val priority: RenderPriority, override val id: String,
    private val clickFunctions: List<GuiFunction>,
    private val dragFunctions: List<GuiFunction>,
    private val closeFunctions: List<GuiFunction>,
    private var hidden: Boolean
) : GuiComponent {
    var context: GuiContext? = null
    override var api: GuiEngineApi? = null
    /**
     * Get the list of click functions for the GUI component.
     *
     * @return The list of click functions.
     */
    /**
     * Get the list of drag functions for the GUI component.
     *
     * @return The list of drag functions.
     */
    @Throws(IOException::class)
    override fun serialize(gen: GeneratorContext, serializers: SerializerProvider) {
        gen.writeStringField("type", type)
        gen.writeStringField("id", id)
        gen.writeStringField("priority", renderPriority().toString())
        gen.writeNumberField("x", offsetX())
        gen.writeNumberField("y", offsetY())
        gen.writeNumberField("width", width())
        gen.writeNumberField("height", height())
        gen.writeBooleanField("hidden", hidden())
        gen.writeFunctionField("on-click", clickFunctions)
        gen.writeFunctionField("on-drag", dragFunctions)
        gen.writeFunctionField("on-close", closeFunctions)
    }

    override fun clickedComponent(event: InventoryClickEvent) {
        event.isCancelled = true
        context?.let { callFunctions(clickFunctions, it) }
    }

    override fun draggedComponent(event: InventoryDragEvent) {
        event.isCancelled = true
        context?.let { callFunctions(dragFunctions, it) }
    }

    override fun closedComponent(event: InventoryCloseEvent) {
        context?.let { callFunctions(closeFunctions, it) }
    }

    override fun renderPriority() = priority

    override fun offsetX() = offsetX

    override fun offsetY() = offsetY

    override fun width() = width

    override fun height() = height

    override fun hidden() = hidden

    override fun setX(x: Int) {
        offsetX = x
    }

    override fun setY(y: Int) {
        offsetY = y
    }

    override fun setHidden(hidden: Boolean) {
        this.hidden = hidden
    }

    override fun setGuiContext(context: GuiContext) {
        this.context = context
    }
}
