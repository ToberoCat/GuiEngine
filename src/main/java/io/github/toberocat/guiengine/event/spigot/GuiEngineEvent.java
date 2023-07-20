package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom GUI engine event that extends the `Event` class from Bukkit.
 * This class is the base class for all GUI-related events in the GUI engine.
 * <p>
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiEngineEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Gets the `HandlerList` for this GUI engine event.
     *
     * @return The `HandlerList` for this event.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final @NotNull GuiContext context;

    /**
     * Constructs a new `GuiEngineEvent` with the associated `GuiContext`.
     *
     * @param context The `GuiContext` associated with the event.
     */
    public GuiEngineEvent(@NotNull GuiContext context) {
        this.context = context;
    }

    /**
     * Gets the `GuiContext` associated with this GUI engine event.
     *
     * @return The `GuiContext` associated with the event.
     */
    public @NotNull GuiContext getContext() {
        return context;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
