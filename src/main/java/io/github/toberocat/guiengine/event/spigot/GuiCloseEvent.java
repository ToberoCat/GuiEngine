package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom GUI close event triggered when a player closes a GUI.
 * It extends the `GuiEngineEvent` class and provides additional information about the close event.
 * <p>
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiCloseEvent extends GuiEngineEvent {

    private final @NotNull InventoryCloseEvent closeEvent;

    /**
     * Constructs a new `GuiCloseEvent` with the associated `GuiContext` and the inventory close event.
     *
     * @param context     The `GuiContext` associated with the event.
     * @param closeEvent  The `InventoryCloseEvent` triggered when the GUI is closed.
     */
    public GuiCloseEvent(@NotNull GuiContext context,
                         @NotNull InventoryCloseEvent closeEvent) {
        super(context);
        this.closeEvent = closeEvent;
    }

    /**
     * Gets the `InventoryCloseEvent` associated with this GUI close event.
     *
     * @return The `InventoryCloseEvent` triggered when the GUI is closed.
     */
    public @NotNull InventoryCloseEvent getCloseEvent() {
        return closeEvent;
    }
}
