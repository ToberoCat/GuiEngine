package io.github.toberocat.guiengine.context

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.action.RenderGuiAction
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.event.GuiDomEvents
import io.github.toberocat.guiengine.event.GuiEventListener
import io.github.toberocat.guiengine.event.GuiEvents
import io.github.toberocat.guiengine.event.spigot.GuiCloseEvent
import io.github.toberocat.guiengine.event.spigot.GuiComponentClickEvent
import io.github.toberocat.guiengine.event.spigot.GuiComponentDragEvent
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.interpreter.GuiInterpreter
import io.github.toberocat.guiengine.xml.XmlComponent
import io.github.toberocat.guiengine.xml.XmlGui
import io.github.toberocat.toberocore.action.Action
import io.github.toberocat.toberocore.util.StreamUtils
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream

/**
 * The `GuiContext` class represents a GUI context that holds information about the GUI components and actions for a
 * specific player.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 04/02/2023
 */
open class GuiContext(
    val interpreter: GuiInterpreter, val api: GuiEngineApi, val xmlGui: XmlGui, private val contextType: ContextType
) : GuiEvents, GuiEventListener {
    val components: MutableList<GuiComponent>

    /**
     * Returns the set of local actions associated with this context.
     *
     * @return The set of local actions.
     */
    val localActions: MutableSet<Action>

    /**
     * Returns the unique identifier of this `GuiContext`.
     *
     * @return The unique identifier.
     */
    val contextId: UUID
    val domEvents: GuiDomEvents
    val computableFunctionProcessor: RenderComputableFunctionProcessor = RenderComputableFunctionProcessor()
    private var inventory: Inventory? = null
    private var viewer: Player? = null

    init {
        components = ArrayList()
        contextId = UUID.randomUUID()
        domEvents = GuiDomEvents(xmlGui)
        localActions = mutableSetOf(RenderGuiAction(this))
        GuiEngineApi.LOADED_CONTEXTS[contextId] = this
    }

    /**
     * Returns a stream of GUI components in ascending order of their render priority.
     *
     * @return A stream of GUI components in ascending order of their render priority.
     */
    fun componentsAscending(): Stream<GuiComponent> =
        components.stream().sorted(Comparator.comparingInt { x: GuiComponent? -> x!!.renderPriority().ordinal })

    /**
     * Returns a stream of GUI components in descending order of their render priority.
     *
     * @return A stream of GUI components in descending order of their render priority.
     */
    fun componentsDescending(): Stream<GuiComponent> = components.stream().sorted { x, y ->
        -x.renderPriority().compareTo(y.renderPriority())
    }

    /**
     * Finds a GUI component with the specified ID.
     *
     * @param id The ID of the GUI component to find.
     * @return The GUI component with the specified ID, or null if not found.
     */
    fun findComponentById(id: String): GuiComponent? = StreamUtils.find(components) { x: GuiComponent? -> x!!.id == id }

    inline fun <reified T : GuiComponent> findComponentByClass() = components.firstOrNull { it is T }.let { it as? T }

    /**
     * Removes a GUI component with the specified ID.
     *
     * @param id The ID of the GUI component to remove.
     */
    fun removeById(id: String) {
        components.remove(findComponentById(id))
    }

    /**
     * Edits an XML component by ID with the specified edit callback.
     *
     * @param id           The ID of the XML component to edit.
     * @param editCallback The callback function to edit the XML component.
     */
    fun editXmlComponentById(id: String, editCallback: Consumer<XmlComponent>) {
        val component = findComponentById(id)
        val index = components.indexOf(component)
        if (null == component || 0 > index) return

        val xml = interpreter().componentToXml(component)
        editCallback.accept(xml)
        val newComponent = interpreter().createComponent(xml, api, this)
        components[index] = newComponent
    }

    /**
     * Adds a GUI component to this context.
     *
     * @param component The GUI component to add.
     */
    fun add(component: GuiComponent) {
        components.add(interpreter().bindComponent(component, api, this))
    }

    /**
     * Adds an XML component to this context.
     *
     * @param component The XML component to add.
     */
    fun add(component: XmlComponent) {
        println(component.javaClass.simpleName)
        components.add(interpreter().createComponent(component, api, this))
    }

    /**
     * Adds multiple XML components to this context.
     *
     * @param components The XML components to add.
     */
    fun add(vararg components: XmlComponent) {
        for (component in components) {
            this.components.add(interpreter().createComponent(component, api, this))
        }
    }

    /**
     * Handles the event when a player clicks on a component in the GUI.
     *
     * @param event The `InventoryClickEvent` representing the click event.
     */
    override fun clickedComponent(event: InventoryClickEvent) {
        interpreter().clickedComponent(event)
        componentsDescending().filter { it.isInComponent(event.slot) }.filter { !it.hidden() }.findFirst()
            .ifPresentOrElse({
                Bukkit.getPluginManager().callEvent(GuiComponentClickEvent(this, event, it))
                it.clickedComponent(event)
            }) { Bukkit.getPluginManager().callEvent(GuiComponentClickEvent(this, event, null)) }
        render()
    }

    /**
     * Handles the event when a player drags an item in the GUI.
     *
     * @param event The `InventoryDragEvent` representing the drag event.
     */
    override fun draggedComponent(event: InventoryDragEvent) {
        componentsDescending().filter { x: GuiComponent? ->
            event.inventorySlots.stream().anyMatch { slot: Int? ->
                x!!.isInComponent(
                    slot!!
                )
            }
        }.filter { x: GuiComponent? -> !x!!.hidden() }.findFirst().ifPresentOrElse({ component: GuiComponent? ->
            Bukkit.getPluginManager().callEvent(GuiComponentDragEvent(this, event, component))
            component!!.draggedComponent(event)
        }) {
            Bukkit.getPluginManager().callEvent(GuiComponentDragEvent(this, event, null))
            interpreter().draggedComponent(event)
        }
        render()
    }

    /**
     * Handles the event when a player closes the GUI.
     *
     * @param event The `InventoryCloseEvent` representing the close event.
     */
    override fun closedComponent(event: InventoryCloseEvent) {
        componentsDescending().filter { x: GuiComponent? -> !x!!.hidden() }
            .forEachOrdered { x: GuiComponent? -> x!!.closedComponent(event) }
        Bukkit.getPluginManager().callEvent(GuiCloseEvent(this, event))
        GuiEngineApi.LOADED_CONTEXTS.remove(contextId)
    }

    /**
     * Returns the associated `GuiInterpreter` for this context.
     *
     * @return The associated `GuiInterpreter`.
     */
    fun interpreter(): GuiInterpreter {
        return interpreter
    }

    /**
     * Returns the viewer (player) associated with this GUI context.
     *
     * @return The viewer (player).
     */
    fun viewer(): Player? = viewer

    /**
     * Returns the inventory associated with this GUI context.
     *
     * @return The inventory.
     */
    fun inventory(): Inventory? = inventory

    /**
     * Sets the inventory for this GUI context.
     *
     * @param inventory The inventory to set.
     */
    fun setInventory(inventory: Inventory?) {
        this.inventory = inventory
    }

    /**
     * Sets the viewer (player) for this GUI context.
     *
     * @param viewer The viewer (player) to set.
     */
    fun setViewer(viewer: Player?) {
        this.viewer = viewer
    }

    /**
     * Renders the GUI context by updating the inventory with the components' content.
     */
    fun render() {
        FunctionProcessor.callFunctions(context.domEvents.onRender, context).get()

        val buffer = interpreter.renderEngine.createBuffer(this)
        interpreter().renderEngine.renderGui(buffer, this, viewer!!)
        val flatContent = inventory!!.contents

        val width = interpreter.renderEngine.width(this)
        for (y in 0 until interpreter.renderEngine.height(this)) System.arraycopy(
            buffer[y], 0, flatContent, y * width, width
        )

        inventory!!.contents = flatContent
    }

    /**
     * Checks if this `GuiContext` is equal to another object.
     *
     * @param other The object to compare with this `GuiContext`.
     * @return `true` if the objects are equal, otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (null == other || javaClass != other.javaClass) return false
        val that = other as GuiContext
        return EqualsBuilder().append(interpreter, that.interpreter).append(components, that.components)
            .append(inventory, that.inventory).append(viewer, that.viewer).isEquals
    }

    /**
     * Generates the hash code for this `GuiContext`.
     *
     * @return The hash code value for this `GuiContext`.
     */
    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37).append(interpreter).append(components).append(inventory).append(viewer)
            .toHashCode()
    }

    /**
     * Converts this `GuiContext` object to a human-readable string representation.
     *
     * @return A string representation of the `GuiContext`.
     */
    override fun toString(): String {
        return try {
            val mapper = ObjectMapper().registerKotlinModule().registerModules(GuiEngineApi.SHARED_MODULES)
            StringJoiner(", ", GuiContext::class.java.getSimpleName() + "[", "]").add("contextId=$contextId")
                .add("interpreter=" + interpreter.interpreterId)
                .add("components=" + mapper.writeValueAsString(components)).add("localActions=$localActions")
                .add("inventory=" + inventory?.size).add("viewerName=" + viewer?.displayName)
                .add("viewerID=" + viewer?.uniqueId).add("inventoryContent=" + inventory?.contents?.contentToString())
                .toString()
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    override val context: GuiContext
        /**
         * Returns the current `GuiContext`.
         *
         * @return The current `GuiContext`.
         */
        get() = this
}
