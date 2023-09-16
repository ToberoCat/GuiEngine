package io.github.toberocat.guiengine.function.compute

import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiComputeFunction
import java.lang.reflect.InvocationTargetException

/**
 * Custom compute function to extract properties of GUI components.
 *
 *
 * Created: 30.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
class GuiComponentPropertyFunction : GuiComputeFunction {

    override fun compute(context: GuiContext, value: String): String {
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

    override fun checkForFunction(value: String): Boolean {
        return value.startsWith(PREFIX)
    }

    companion object {
        private const val PREFIX = "#"
    }
}
