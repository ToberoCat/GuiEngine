package io.github.toberocat.guiengine.function;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Represents a function that can be called on a GUI context.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiFunction {
    static @NotNull GuiFunction of(BiConsumer<GuiEngineApi, GuiContext> method) {
        return new GuiFunction() {
            @Override
            public @NotNull String getType() {
                return "anonymous";
            }

            @Override
            public void call(@NotNull GuiEngineApi api, @NotNull GuiContext context) {
                method.accept(api, context);
            }
        };
    }

    @NotNull String getType();

    /**
     * Calls the GUI function with the specified API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context on which the function is called.
     */
    void call(@NotNull GuiEngineApi api, @NotNull GuiContext context);
}
