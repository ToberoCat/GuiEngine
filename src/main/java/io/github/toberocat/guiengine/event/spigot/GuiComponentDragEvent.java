package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a custom GUI component drag event triggered when a player drags items in the GUI.
 * It extends the `GuiEngineEvent`
 * class and provides additional information about the drag event and the target component.
 * <p>
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiComponentDragEvent extends GuiEngineEvent {

    private final @NotNull InventoryDragEvent dragEvent;
    private final @Nullable GuiComponent targetComponent;

    /**
     * Constructs a new `GuiComponentDragEvent` with the associated `GuiContext`, drag event, and target component (if any).
     *
     * @param context         The `GuiContext` associated with the event.
     * @param dragEvent       The `InventoryDragEvent` triggered when a player drags items in the GUI.
     * @param targetComponent The target `GuiComponent` where the dragging started, or null if no component was targeted.
     */
    public GuiComponentDragEvent(@NotNull GuiContext context, @NotNull InventoryDragEvent dragEvent, @Nullable GuiComponent targetComponent) {
        super(context);
        this.dragEvent = dragEvent;
        this.targetComponent = targetComponent;
    }

    /**
     * Gets the `InventoryDragEvent` associated with this GUI component drag event.
     *
     * @return The `InventoryDragEvent` triggered when a player drags items in the GUI.
     */
    public @NotNull InventoryDragEvent getDragEvent() {
        return dragEvent;
    }

    /**
     * Gets the target `GuiComponent` where the dragging started.
     *
     * @return The target `GuiComponent`, or null if no component was targeted.
     */
    public @Nullable GuiComponent getTargetComponent() {
        return targetComponent;
    }
}
