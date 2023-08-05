package io.github.toberocat.guiengine.function;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a function that can be called on a GUI context.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiFunction {

    @NotNull String getType();

    /**
     * Calls the GUI function with the specified API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context on which the function is called.
     */
    void call(@NotNull GuiEngineApi api, @NotNull GuiContext context);
}
