package io.github.toberocat.guiengine.event;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.event.spigot.GuiEngineEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Represents a listener for GUI-related events.
 * It provides a method to register a listener for a specific event class, and the associated event handler.
 * <p>
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiEventListener {

    /**
     * Gets the associated `GuiContext` for the listener.
     *
     * @return The `GuiContext` associated with the listener.
     */
    @NotNull GuiContext getContext();

    /**
     * Registers a listener for a specific event class along with its associated event handler.
     * The `listener` parameter is a consumer that handles the event when it is triggered.
     * The event will only be handled if the event class is assignable from the actual event's class,
     * and if the context of the event matches the context associated with this `GuiEventListener`.
     *
     * @param clazz    The class of the event to listen for.
     * @param listener The event handler that consumes the event when triggered.
     * @param <E>      The type of the event.
     */
    default <E extends GuiEngineEvent> void listen(@NotNull Class<E> clazz, @NotNull Consumer<E> listener) {
        HandlerList handlerList;
        try {
            handlerList = (HandlerList) clazz.getMethod("getHandlerList").invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        handlerList.register(new RegisteredListener(new Listener() {
        }, (bukkitListener, event) -> {
            if (!clazz.isAssignableFrom(event.getClass())) return;
            E e = clazz.cast(event);
            if (e.getContext() != getContext()) return;

            listener.accept(e);
        }, EventPriority.NORMAL, GuiEngineApiPlugin.getPlugin(), false));
    }
}
