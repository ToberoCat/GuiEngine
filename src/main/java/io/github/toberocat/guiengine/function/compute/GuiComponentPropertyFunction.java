package io.github.toberocat.guiengine.function.compute;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.ComputeFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Custom compute function to extract properties of GUI components.
 * <p>
 * Created: 30.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiComponentPropertyFunction implements ComputeFunction {
    private static final String PREFIX = "#";

    /**
     * Computes the value of the provided placeholder, which represents a property of a GUI component.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the placeholder to compute.
     * @return The computed value of the placeholder representing the property of a GUI component.
     */
    @Override
    public @NotNull String compute(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull String value) {
        String without = value.replace(PREFIX, "");
        String[] split = without.split("\\.");
        if (split.length != 2) return "REQUIRES FORMAT {#id.method}";
        String id = split[0];
        String methodName = split[1];
        GuiComponent component = context.findComponentById(id);
        if (component == null) return "NO COMPONENT FOUND";

        try {
            return component.getClass().getMethod(methodName).invoke(component).toString();
        } catch (NoSuchMethodException | IllegalAccessException e) {
            return "NO METHOD FOUND";
        } catch (InvocationTargetException e) {
            return "EXCEPTION WHILE EXECUTING";
        }
    }

    /**
     * Checks if the provided value contains the function to extract a GUI component property.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    @Override
    public boolean checkForFunction(@NotNull String value) {
        return value.startsWith(PREFIX);
    }
}
