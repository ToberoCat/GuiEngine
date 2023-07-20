package io.github.toberocat.guiengine.function;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for defining custom compute functions for GUI placeholders.
 * <p>
 * Created: 30.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface ComputeFunction {
    /**
     * Computes the value of the provided placeholder.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the placeholder to compute.
     * @return The computed value of the placeholder.
     */
    @NotNull String compute(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull String value);

    /**
     * Checks if the provided value contains the function to be computed.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    boolean checkForFunction(@NotNull String value);
}
