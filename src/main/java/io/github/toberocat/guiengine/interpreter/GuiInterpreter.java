package io.github.toberocat.guiengine.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.event.GuiEvents;
import io.github.toberocat.guiengine.render.GuiRenderEngine;
import io.github.toberocat.guiengine.xml.XmlComponent;
import io.github.toberocat.guiengine.xml.XmlGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
