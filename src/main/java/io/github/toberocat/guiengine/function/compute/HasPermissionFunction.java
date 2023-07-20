package io.github.toberocat.guiengine.function.compute;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.ComputeFunction;
import org.jetbrains.annotations.NotNull;

/**
 * Custom compute function to check if a player has a specific permission.
 * <p>
 * Created: 14.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class HasPermissionFunction implements ComputeFunction {
    private static final String PREFIX = "@";

    /**
     * Computes the result of the permission check using the provided API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the permission node to check.
     * @return A string representation of the result with the permission check.
     * `True` if the player has the specified permission, `false` otherwise.
     */
    @Override
    public @NotNull String compute(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull String value) {
        String permission = value.replace(PREFIX, "");
        return String.valueOf(context.viewer() != null && context.viewer().hasPermission(permission));
    }

    /**
     * Checks if the provided value contains the function to check for the presence of a permission.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    @Override
    public boolean checkForFunction(@NotNull String value) {
        return value.startsWith(PREFIX);
    }
}
