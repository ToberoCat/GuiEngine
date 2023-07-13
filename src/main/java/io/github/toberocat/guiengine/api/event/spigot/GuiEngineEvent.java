package io.github.toberocat.guiengine.api.event.spigot;

import io.github.toberocat.guiengine.api.context.GuiContext;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiEngineEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final @NotNull GuiContext context;

    public GuiEngineEvent(@NotNull GuiContext context) {
        this.context = context;
    }

    public @NotNull GuiContext getContext() {
        return context;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
