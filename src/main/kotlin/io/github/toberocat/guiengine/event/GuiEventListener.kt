package io.github.toberocat.guiengine.event

import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.event.spigot.GuiEngineEvent
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.RegisteredListener
import java.lang.reflect.InvocationTargetException
import java.util.function.Consumer

/**
 * Represents a listener for GUI-related events.
 * It provides a method to register a listener for a specific event class, and the associated event handler.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiEventListener {
    /**
     * Gets the associated `GuiContext` for the listener.
     *
     * @return The `GuiContext` associated with the listener.
     */
    val context: GuiContext

    /**
     * Registers a listener for a specific event class along with its associated event handler.
     * The `listener` parameter is a consumer that handles the event when it is triggered.
     * The event will only be handled if the event class is assignable from the actual event's class,
     * and if the context of the event matches the context associated with this `GuiEventListener`.
     *
     * @param clazz    The class of the event to listen for.
     * @param listener The event handler that consumes the event when triggered.
     * @param <E> The type of the event.
    </E> */
    fun <E : GuiEngineEvent?> listen(clazz: Class<E>, listener: Consumer<E>) {
        val handlerList: HandlerList = try {
            clazz.getMethod("getHandlerList").invoke(null) as HandlerList
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

        handlerList.register(
            RegisteredListener(
                object : Listener {},
                EventExecutor { _: Listener?, event: Event ->
                    if (!clazz.isAssignableFrom(event.javaClass)) return@EventExecutor
                    val e = clazz.cast(event)
                    if (e!!.context !== context) return@EventExecutor
                    listener.accept(e)
                },
                EventPriority.NORMAL,
                plugin,
                false
            )
        )
    }
}
