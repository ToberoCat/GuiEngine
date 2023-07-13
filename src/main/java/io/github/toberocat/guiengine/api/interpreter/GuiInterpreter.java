package io.github.toberocat.guiengine.api.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.event.GuiEvents;
import io.github.toberocat.guiengine.api.render.GuiRenderEngine;
import io.github.toberocat.guiengine.api.xml.XmlComponent;
import io.github.toberocat.guiengine.api.xml.XmlGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiInterpreter extends GuiEvents {
    @NotNull String interpreterId();

    @NotNull GuiRenderEngine getRenderEngine();

    @NotNull GuiContext loadContent(@NotNull GuiEngineApi api,
                                    @NotNull Player viewer,
                                    @NotNull XmlGui xmlGui);

    @Nullable GuiComponent createComponent(@NotNull XmlComponent xmlComponent,
                                           @NotNull GuiEngineApi api,
                                           @NotNull GuiContext context);
    @NotNull GuiComponent bindComponent(@NotNull GuiComponent component,
                                        @NotNull GuiEngineApi api,
                                        @NotNull GuiContext context);

    @NotNull XmlComponent xmlComponent(@NotNull JsonNode node,
                                       @NotNull GuiEngineApi api) throws JsonProcessingException;

    @NotNull XmlComponent componentToXml(@NotNull GuiEngineApi api,
                                         @NotNull GuiComponent component);
}
