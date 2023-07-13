package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiComponentDragEvent extends GuiEngineEvent {

    private final @NotNull InventoryDragEvent dragEvent;

    private final @Nullable GuiComponent targetComponent;

    public GuiComponentDragEvent(@NotNull GuiContext context,
                                 @NotNull InventoryDragEvent dragEvent,
                                 @Nullable GuiComponent targetComponent) {
        super(context);
        this.dragEvent = dragEvent;
        this.targetComponent = targetComponent;
    }

    public @NotNull InventoryDragEvent getDragEvent() {
        return dragEvent;
    }

    public @Nullable GuiComponent getTargetComponent() {
        return targetComponent;
    }
}
