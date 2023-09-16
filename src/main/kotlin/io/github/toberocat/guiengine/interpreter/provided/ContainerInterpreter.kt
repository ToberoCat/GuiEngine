package io.github.toberocat.guiengine.interpreter.provided

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.interpreter.GuiInterpreter
import io.github.toberocat.guiengine.utils.nullCheck
import io.github.toberocat.guiengine.xml.XmlComponent
import io.github.toberocat.guiengine.xml.XmlGui
import org.bukkit.entity.Player

interface ContainerInterpreter : GuiInterpreter {

    fun containerContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui): GuiContext

    override fun createContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui): GuiContext {
        val context = containerContext(api, viewer, xmlGui)
        if (xmlGui["implicit-update"].map { it.asBoolean() }.orElse(true))
            context.domEvents.onRender.add(GuiFunction.anonymous { it.computableFunctionProcessor.editAll(it) })
        
        return context
    }

    override fun loadContent(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui): GuiContext {
        val context = createContext(api, viewer, xmlGui)
        context.setViewer(viewer)

        for (component in xmlGui.components) {
            val guiComponent = createComponent(component, api, context)
            context.components.add(guiComponent)
        }
        return context
    }

    override fun createComponent(
        xmlComponent: XmlComponent, api: GuiEngineApi, context: GuiContext
    ): GuiComponent {
        val map: MutableMap<String, Any> = xmlComponent.objectFields(api) { node: JsonNode? -> node }
        map["__:api:__"] = api.id
        map["__:ctx:__"] = context.contextId

        val componentClass = api.getComponentIdMap()[xmlComponent.type]
            ?: throw InvalidGuiComponentException("Type ${xmlComponent.type} isn't recognized as a component")
        return bindComponent(api.xmlMapper.convertValue(map, componentClass), api, context)
    }

    override fun bindComponent(
        component: GuiComponent, api: GuiEngineApi, context: GuiContext
    ): GuiComponent {
        component.api = api
        component.setGuiContext(context)
        component.addActions(context.localActions)
        return component
    }

    @Throws(JsonProcessingException::class)
    override fun xmlComponent(
        node: JsonNode, api: GuiEngineApi
    ): XmlComponent = api.xmlMapper.treeToValue(node, XmlComponent::class.java)

    override fun componentToXml(
        component: GuiComponent
    ): XmlComponent = component.api?.xmlMapper?.convertValue(component, XmlComponent::class.java)
        .nullCheck("Api not set yet")
}
