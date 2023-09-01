package io.github.toberocat.guiengine.interpreter

/**
 * This class represents the InterpreterManager, responsible for managing and providing access to various
 * implementations of the GuiInterpreter interface.
 *
 *
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class InterpreterManager {
    /**
     * A map that associates interpreter IDs with corresponding GuiInterpreter instances.
     */
    private val interpreterMap: MutableMap<String, GuiInterpreter>

    /**
     * Creates a new InterpreterManager with an empty interpreter map.
     */
    init {
        interpreterMap = HashMap()
    }

    /**
     * Retrieves the GuiInterpreter associated with the given interpreter ID.
     *
     * @param interpreter The ID of the GuiInterpreter to retrieve.
     * @return The corresponding GuiInterpreter instance if found, or null if not present in the manager.
     */
    fun getInterpreter(interpreter: String): GuiInterpreter? {
        return interpreterMap[interpreter]
    }

    /**
     * Registers a new GuiInterpreter in the manager.
     *
     * @param interpreter The GuiInterpreter instance to be registered.
     */
    fun registerInterpreter(interpreter: GuiInterpreter) {
        interpreterMap[interpreter.interpreterId] = interpreter
    }
}
