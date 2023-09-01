package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.ComputeFunction
import java.lang.reflect.InvocationTargetException

/**
 * Custom compute function to extract properties of GUI components.
 *
 *
 * Created: 30.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiComponentPropertyFunction : ComputeFunction {
    /**
     * Computes the value of the provided placeholder, which represents a attribute of a GUI component.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing the placeholder to compute.
     * @return The computed value of the placeholder representing the attribute of a GUI component.
     */
    override fun compute(api: GuiEngineApi, context: GuiContext, value: String): String {
        val without = value.replace(PREFIX, "")
        val split = without.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (2 != split.size) return "REQUIRES FORMAT {#id.method}"
        val id = split[0]
        val methodName = split[1]
        val component = context.findComponentById(id) ?: return "NO COMPONENT FOUND"
        return try {
            component.javaClass.getMethod(methodName).invoke(component).toString()
        } catch (e: NoSuchMethodException) {
            "NO METHOD FOUND"
        } catch (e: IllegalAccessException) {
            "NO METHOD FOUND"
        } catch (e: InvocationTargetException) {
            "EXCEPTION WHILE EXECUTING"
        }
    }

    /**
     * Checks if the provided value contains the function to extract a GUI component attribute.
     *
     * @param value The input value to check for the function.
     * @return `true` if the function is present in the value, `false` otherwise.
     */
    override fun checkForFunction(value: String): Boolean {
        return value.startsWith(PREFIX)
    }

    companion object {
        private const val PREFIX = "#"
    }
}
