package io.github.toberocat.guiengine.event.spigot;

import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a custom GUI component click event triggered when a player clicks on a GUI component.
 * It extends the `GuiEngineEvent`
 * class and provides additional information about the click event and the target component.
 * <p>
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class GuiComponentClickEvent extends GuiEngineEvent {

    private final @NotNull InventoryClickEvent clickEvent;
    private final @Nullable GuiComponent targetComponent;

    /**
     * Constructs a new `GuiComponentClickEvent` with the associated `GuiContext`, click event, and target component (if any).
     *
     * @param context         The `GuiContext` associated with the event.
     * @param clickEvent      The `InventoryClickEvent` triggered when a player clicks on the GUI.
     * @param targetComponent The target `GuiComponent` that was clicked, or null if no component was targeted.
     */
    public GuiComponentClickEvent(@NotNull GuiContext context, @NotNull InventoryClickEvent clickEvent, @Nullable GuiComponent targetComponent) {
        super(context);
        this.clickEvent = clickEvent;
        this.targetComponent = targetComponent;
    }

    /**
     * Gets the `InventoryClickEvent` associated with this GUI component click event.
     *
     * @return The `InventoryClickEvent` triggered when a player clicks on the GUI.
     */
    public @NotNull InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    /**
     * Gets the target `GuiComponent` that was clicked by the player.
     *
     * @return The target `GuiComponent`, or null if no component was targeted.
     */
    public @Nullable GuiComponent getTargetComponent() {
        return targetComponent;
    }
}
