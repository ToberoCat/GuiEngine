package io.github.toberocat.guiengine.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.xml.XmlComponent;
import io.github.toberocat.guiengine.xml.XmlGui;
import io.github.toberocat.guiengine.render.DefaultGuiRenderEngine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Created: 05/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class DefaultInterpreter implements GuiInterpreter {

    @Override
    public @NotNull String interpreterId() {
        return "default";
    }

    @Override
    public @NotNull DefaultGuiRenderEngine getRenderEngine() {
        return new DefaultGuiRenderEngine();
    }

    @Override
    public @NotNull GuiContext loadContent(@NotNull GuiEngineApi api,
                                           @NotNull Player viewer,
                                           @NotNull XmlGui xmlGui) {
        GuiContext context = new GuiContext(this,
                xmlGui.getTitle(),
                xmlGui.getWidth(),
                xmlGui.getHeight());

        for (XmlComponent component : xmlGui.getComponents()) {
            GuiComponent guiComponent = createComponent(component, api, context);
            if (guiComponent == null)
                continue;
            context.components().add(guiComponent);
        }

        return context;
    }

    @Override
    public @Nullable GuiComponent createComponent(@NotNull XmlComponent xmlComponent,
                                                  @NotNull GuiEngineApi api,
                                                  @NotNull GuiContext context) {
        return bindComponent(api.getXmlMapper().convertValue(xmlComponent.objectFields(api, node -> node),
                api.getComponentIdMap().get(xmlComponent.type())), api, context);
    }

    @Override
    public @NotNull GuiComponent bindComponent(@NotNull GuiComponent component,
                                               @NotNull GuiEngineApi api,
                                               @NotNull GuiContext context) {
        component.setApi(api);
        component.setContext(context);
        component.addActions(context.getLocalActions());
        return component;
    }

    @Override
    public @NotNull XmlComponent xmlComponent(@NotNull JsonNode node, @NotNull GuiEngineApi api)
            throws JsonProcessingException {
        return api.getXmlMapper().treeToValue(node, XmlComponent.class);
    }

    @Override
    public @NotNull XmlComponent componentToXml(@NotNull GuiEngineApi api,
                                                @NotNull GuiComponent component) {
        return api.getXmlMapper().convertValue(component, XmlComponent.class);
    }
}
