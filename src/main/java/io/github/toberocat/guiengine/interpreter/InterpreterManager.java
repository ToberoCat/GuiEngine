package io.github.toberocat.guiengine.interpreter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class InterpreterManager {
    private final @NotNull Map<String, GuiInterpreter> interpreterMap;

    public InterpreterManager() {
        this.interpreterMap = new HashMap<>();
    }

    public @Nullable GuiInterpreter getInterpreter(@NotNull String interpreter) {
        return interpreterMap.get(interpreter);
    }
    public void registerInterpreter(@NotNull GuiInterpreter interpreter) {
        interpreterMap.put(interpreter.interpreterId(), interpreter);
    }
}
