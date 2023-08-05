package io.github.toberocat.guiengine.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
import io.github.toberocat.guiengine.render.DefaultGuiRenderEngine;
import io.github.toberocat.guiengine.xml.XmlComponent;
import io.github.toberocat.guiengine.xml.XmlGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DefaultInterpreter is an implementation of the GuiInterpreter interface that provides default
 * interpretation and handling of GUI components defined in XML format.
 * <p>
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public class DefaultInterpreter implements GuiInterpreter {

    /**
     * Returns the identifier for this interpreter.
     *
     * @return The identifier "default" for this interpreter.
     */
    @Override
    public @NotNull String interpreterId() {
        return "default";
    }

    /**
     * Retrieves the DefaultGuiRenderEngine used by this interpreter to render GUI components.
     *
     * @return A new instance of DefaultGuiRenderEngine.
     */
    @Override
    public @NotNull DefaultGuiRenderEngine getRenderEngine() {
        return new DefaultGuiRenderEngine();
    }

    /**
     * Loads the GUI content from the given XmlGui and creates a GuiContext with the associated components.
     *
     * @param api    The GuiEngineApi instance used for interacting with the GUI engine.
     * @param viewer The Player who will view the GUI.
     * @param xmlGui The XmlGui instance representing the GUI layout.
     * @return A GuiContext object representing the loaded GUI context.
     */
    @Override
    public @NotNull GuiContext loadContent(@NotNull GuiEngineApi api, @NotNull Player viewer, @NotNull XmlGui xmlGui) {
        GuiContext context = new GuiContext(this, xmlGui.getTitle(), xmlGui.getWidth(), xmlGui.getHeight());
        context.setViewer(viewer);

        if (xmlGui.getComponents() == null)
            return context;

        for (XmlComponent component : xmlGui.getComponents()) {
            GuiComponent guiComponent = createComponent(component, api, context);
            if (null == guiComponent) continue;
            context.components().add(guiComponent);
        }

        return context;
    }

    /**
     * Creates a GuiComponent from the given XmlComponent and binds it to the provided GuiEngineApi and GuiContext.
     *
     * @param xmlComponent The XmlComponent to create the GuiComponent from.
     * @param api          The GuiEngineApi instance used for interacting with the GUI engine.
     * @param context      The GuiContext in which the GuiComponent will be bound.
     * @return The created GuiComponent instance, or null if creation failed.
     * @throws InvalidGuiComponentException If the XmlComponent type is not recognized as a valid component.
     */
    @Override
    public @Nullable GuiComponent createComponent(@NotNull XmlComponent xmlComponent, @NotNull GuiEngineApi api, @NotNull GuiContext context) {
        Map<String, Object> map = xmlComponent.objectFields(api, node -> node);
        map.put("__:api:__", api.getId());
        map.put("__:ctx:__", context.getContextId());

        Class<? extends GuiComponent> componentClass = api.getComponentIdMap().get(xmlComponent.type());
        if (null == componentClass)
            throw new InvalidGuiComponentException(String.format("Type %s isn't recognized as a component", xmlComponent.type()));
        return bindComponent(api.getXmlMapper().convertValue(map, componentClass), api, context);
    }

    /**
     * Binds a GuiComponent to the provided GuiEngineApi and GuiContext, and adds actions from the context's local actions.
     *
     * @param component The GuiComponent to bind.
     * @param api       The GuiEngineApi instance used for interacting with the GUI engine.
     * @param context   The GuiContext to which the GuiComponent will be bound.
     * @return The bound GuiComponent.
     */
    @Override
    public @NotNull GuiComponent bindComponent(@NotNull GuiComponent component, @NotNull GuiEngineApi api, @NotNull GuiContext context) {
        component.setApi(api);
        component.setContext(context);
        component.addActions(context.getLocalActions());
        return component;
    }

    /**
     * Converts a JsonNode to an XmlComponent using the provided GuiEngineApi.
     *
     * @param node The JsonNode to convert to XmlComponent.
     * @param api  The GuiEngineApi instance used for converting the JsonNode.
     * @return The converted XmlComponent.
     * @throws JsonProcessingException If there is an issue processing the JsonNode.
     */
    @Override
    public @NotNull XmlComponent xmlComponent(@NotNull JsonNode node, @NotNull GuiEngineApi api) throws JsonProcessingException {
        return api.getXmlMapper().treeToValue(node, XmlComponent.class);
    }

    /**
     * Converts a GuiComponent to an XmlComponent using the provided GuiEngineApi.
     *
     * @param api       The GuiEngineApi instance used for converting the GuiComponent.
     * @param component The GuiComponent to convert to XmlComponent.
     * @return The converted XmlComponent.
     */
    @Override
    public @NotNull XmlComponent componentToXml(@NotNull GuiEngineApi api, @NotNull GuiComponent component) {
        return api.getXmlMapper().convertValue(component, XmlComponent.class);
    }
}
