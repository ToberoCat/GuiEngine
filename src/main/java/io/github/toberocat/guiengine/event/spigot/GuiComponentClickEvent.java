package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentClickEvent extends GuiEngineEvent {

    private final @NotNull InventoryClickEvent clickEvent;
    private final @Nullable GuiComponent targetComponent;

    public GuiComponentClickEvent(@NotNull GuiContext context,
                                  @NotNull InventoryClickEvent clickEvent,
                                  @Nullable GuiComponent targetComponent) {
        super(context);
        this.clickEvent = clickEvent;
        this.targetComponent = targetComponent;
    }

    public @NotNull InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    public @Nullable GuiComponent getTargetComponent() {
        return targetComponent;
    }
}
