package io.github.toberocat.guiengine.interpreter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.event.GuiEvents
import io.github.toberocat.guiengine.render.GuiRenderEngine
import io.github.toberocat.guiengine.xml.XmlComponent
import io.github.toberocat.guiengine.xml.XmlGui
import org.bukkit.entity.Player

/**
 * Custom GUI interpreter to process XML-based GUI definitions and handle GUI components.
 *
 *
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiInterpreter : GuiEvents {
    val interpreterId: String
    val renderEngine: GuiRenderEngine

    fun createContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui): GuiContext

    /**
     * Loads the content of a GUI based on the provided XML GUI definition and player viewer.
     *
     * @param api    The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param viewer The `Player` representing the viewer of the GUI.
     * @param xmlGui The `XmlGui` instance representing the XML definition of the GUI.
     * @return The `GuiContext` representing the loaded GUI content.
     */
    fun loadContent(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui): GuiContext

    /**
     * Creates a GUI component based on the provided XML component definition.
     *
     * @param xmlComponent The `XmlComponent` instance representing the XML definition of the component.
     * @param api          The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context      The `GuiContext` instance representing the current GUI context.
     * @return The `GuiComponent` instance representing the created GUI component.
     */
    fun createComponent(xmlComponent: XmlComponent, api: GuiEngineApi, context: GuiContext): GuiComponent

    /**
     * Binds an existing GUI component to the specified GUI context.
     *
     * @param component The `GuiComponent` instance to be bound.
     * @param api       The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context   The `GuiContext` instance representing the current GUI context.
     * @return The `GuiComponent` instance representing the bound GUI component.
     */
    fun bindComponent(component: GuiComponent, api: GuiEngineApi, context: GuiContext): GuiComponent

    /**
     * Converts a JSON node representing a GUI component into an `XmlComponent` instance.
     *
     * @param node The JSON node representing the GUI component.
     * @param api  The `GuiEngineApi` instance used to interact with the GUI engine.
     * @return The `XmlComponent` instance representing the GUI component.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @Throws(JsonProcessingException::class)
    fun xmlComponent(node: JsonNode, api: GuiEngineApi): XmlComponent

    /**
     * Converts a `GuiComponent` instance into its XML representation.
     *
     * @param component The `GuiComponent` instance to be converted.
     * @return The `XmlComponent` instance representing the XML representation of the GUI component.
     */
    fun componentToXml(component: GuiComponent): XmlComponent
}