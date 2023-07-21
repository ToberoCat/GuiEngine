package io.github.toberocat.guiengine.interpreter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the InterpreterManager, responsible for managing and providing access to various
 * implementations of the GuiInterpreter interface.
 * <p>
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class InterpreterManager {

    /**
     * A map that associates interpreter IDs with corresponding GuiInterpreter instances.
     */
    private final @NotNull Map<String, GuiInterpreter> interpreterMap;

    /**
     * Creates a new InterpreterManager with an empty interpreter map.
     */
    public InterpreterManager() {
        this.interpreterMap = new HashMap<>();
    }

    /**
     * Retrieves the GuiInterpreter associated with the given interpreter ID.
     *
     * @param interpreter The ID of the GuiInterpreter to retrieve.
     * @return The corresponding GuiInterpreter instance if found, or null if not present in the manager.
     */
    public @Nullable GuiInterpreter getInterpreter(@NotNull String interpreter) {
        return interpreterMap.get(interpreter);
    }

    /**
     * Registers a new GuiInterpreter in the manager.
     *
     * @param interpreter The GuiInterpreter instance to be registered.
     */
    public void registerInterpreter(@NotNull GuiInterpreter interpreter) {
        interpreterMap.put(interpreter.interpreterId(), interpreter);
    }
}

