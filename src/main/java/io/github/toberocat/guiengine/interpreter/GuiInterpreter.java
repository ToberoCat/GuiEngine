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
 * Custom GUI interpreter to process XML-based GUI definitions and handle GUI components.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiInterpreter extends GuiEvents {

    /**
     * Retrieves the unique identifier of the interpreter.
     *
     * @return The unique identifier of the interpreter.
     */
    @NotNull String interpreterId();

    /**
     * Retrieves the render engine associated with this interpreter.
     *
     * @return The render engine associated with this interpreter.
     */
    @NotNull GuiRenderEngine getRenderEngine();

    /**
     * Loads the content of a GUI based on the provided XML GUI definition and player viewer.
     *
     * @param api    The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param viewer The `Player` representing the viewer of the GUI.
     * @param xmlGui The `XmlGui` instance representing the XML definition of the GUI.
     * @return The `GuiContext` representing the loaded GUI content.
     */
    @NotNull GuiContext loadContent(@NotNull GuiEngineApi api, @NotNull Player viewer, @NotNull XmlGui xmlGui);

    /**
     * Creates a GUI component based on the provided XML component definition.
     *
     * @param xmlComponent The `XmlComponent` instance representing the XML definition of the component.
     * @param api          The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context      The `GuiContext` instance representing the current GUI context.
     * @return The `GuiComponent` instance representing the created GUI component.
     */
    @Nullable GuiComponent createComponent(@NotNull XmlComponent xmlComponent, @NotNull GuiEngineApi api, @NotNull GuiContext context);

    /**
     * Binds an existing GUI component to the specified GUI context.
     *
     * @param component The `GuiComponent` instance to be bound.
     * @param api       The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context   The `GuiContext` instance representing the current GUI context.
     * @return The `GuiComponent` instance representing the bound GUI component.
     */
    @NotNull GuiComponent bindComponent(@NotNull GuiComponent component, @NotNull GuiEngineApi api, @NotNull GuiContext context);

    /**
     * Converts a JSON node representing a GUI component into an `XmlComponent` instance.
     *
     * @param node The JSON node representing the GUI component.
     * @param api  The `GuiEngineApi` instance used to interact with the GUI engine.
     * @return The `XmlComponent` instance representing the GUI component.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @NotNull XmlComponent xmlComponent(@NotNull JsonNode node, @NotNull GuiEngineApi api) throws JsonProcessingException;

    /**
     * Converts a `GuiComponent` instance into its XML representation.
     *
     * @param api       The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param component The `GuiComponent` instance to be converted.
     * @return The `XmlComponent` instance representing the XML representation of the GUI component.
     */
    @NotNull XmlComponent componentToXml(@NotNull GuiEngineApi api, @NotNull GuiComponent component);
}