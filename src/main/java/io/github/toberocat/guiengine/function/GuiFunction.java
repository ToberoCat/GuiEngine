package io.github.toberocat.guiengine.function;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
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
