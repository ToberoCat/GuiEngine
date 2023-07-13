package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiCloseEvent extends GuiEngineEvent {

    private final @NotNull InventoryCloseEvent closeEvent;

    public GuiCloseEvent(@NotNull GuiContext context,
                         @NotNull InventoryCloseEvent closeEvent) {
        super(context);
        this.closeEvent = closeEvent;
    }

    public @NotNull InventoryCloseEvent getCloseEvent() {
        return closeEvent;
    }
}
