package io.github.toberocat.guiengine.api.event.spigot;

import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.context.GuiContext;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
