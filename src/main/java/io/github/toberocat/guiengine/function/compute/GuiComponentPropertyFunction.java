package io.github.toberocat.guiengine.function.compute;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.ComputeFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentPropertyFunction implements ComputeFunction {
    private static final String PREFIX = "#";

    @Override
    public @NotNull String compute(@NotNull GuiEngineApi api,
                                   @NotNull GuiContext context,
                                   @NotNull String value) {
        String without = value.replace(PREFIX, "");
        String[] split = without.split("\\.");
        if (split.length != 2)
            return "REQUIRES FORMAT {#id.method}";
        String id = split[0];
        String methodName = split[1];
        GuiComponent component = context.findComponentById(id);
        if (component == null)
            return "NO COMPONENT FOUND";

        try {
            return component.getClass().getMethod(methodName).invoke(component).toString();
        } catch (NoSuchMethodException | IllegalAccessException e) {
            return "NO METHOD FOUND";
        } catch (InvocationTargetException e) {
            return "EXCEPTION WHILE EXECUTING";
        }
    }

    @Override
    public boolean checkForFunction(@NotNull String value) {
        return value.startsWith(PREFIX);
    }
}
