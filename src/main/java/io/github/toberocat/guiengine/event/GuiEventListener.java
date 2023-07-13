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
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiEventListener {

    @NotNull GuiContext getContext();

    default <E extends GuiEngineEvent> void listen(@NotNull Class<E> clazz, @NotNull Consumer<E> listener) {
        HandlerList handlerList;
        try {
            handlerList = (HandlerList) clazz.getMethod("getHandlerList").invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        handlerList.register(new RegisteredListener(new Listener() {}, (bukkitListener, event) -> {
            if (!clazz.isAssignableFrom(event.getClass()))
                return;
            E e = clazz.cast(event);
            if (e.getContext() != getContext())
                return;

            listener.accept(e);
        }, EventPriority.NORMAL, GuiEngineApiPlugin.getPlugin(), false));
    }
}
