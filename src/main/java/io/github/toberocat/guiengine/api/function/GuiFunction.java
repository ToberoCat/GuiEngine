package io.github.toberocat.guiengine.api.function;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.context.GuiContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiFunction {
    void call(@NotNull GuiEngineApi api,
                      @NotNull GuiContext context);
}
