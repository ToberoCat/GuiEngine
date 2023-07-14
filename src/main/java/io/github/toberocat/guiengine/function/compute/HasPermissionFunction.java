package io.github.toberocat.guiengine.function.compute;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.ComputeFunction;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 14.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class HasPermissionFunction implements ComputeFunction {
    private static final String PREFIX = "@";

    @Override
    public @NotNull String compute(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull String value) {
        String permission = value.replace(PREFIX, "");
        return String.valueOf(context.viewer() != null && context.viewer().hasPermission(permission));
    }

    @Override
    public boolean checkForFunction(@NotNull String value) {
        return value.startsWith(PREFIX);
    }
}
