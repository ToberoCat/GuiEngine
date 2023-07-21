package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.event.GuiEvents;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.CoordinatePair;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.Utils;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

/**
 * Represents a GUI component that can be rendered and interacted with in the graphical user interface.
 * This interface extends the GuiEvents interface, providing methods for handling GUI events related to the component.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiComponent extends GuiEvents {

    /**
     * Get the ID of the GUI component.
     *
     * @return The ID of the GUI component.
     */
    @NotNull String getId();

    /**
     * Get the type of the GUI component.
     *
     * @return The type of the GUI component.
     */
    @NotNull String getType();

    /**
     * Get the rendering priority of the GUI component.
     * The rendering priority determines the order in which the component is rendered relative to other components.
     *
     * @return The rendering priority of the GUI component.
     */
    @NotNull RenderPriority renderPriority();

    /**
     * Check if the GUI component is hidden.
     *
     * @return true if the component is hidden, false otherwise.
     */
    boolean hidden();

    /**
     * Get the X offset of the GUI component.
     * The X offset represents the horizontal position of the component within the GUI layout.
     *
     * @return The X offset of the GUI component.
     */
    int offsetX();

    /**
     * Get the Y offset of the GUI component.
     * The Y offset represents the vertical position of the component within the GUI layout.
     *
     * @return The Y offset of the GUI component.
     */
    int offsetY();

    /**
     * Get the width of the GUI component.
     *
     * @return The width of the GUI component.
     */
    int width();

    /**
     * Get the height of the GUI component.
     *
     * @return The height of the GUI component.
     */
    int height();

    /**
     * Render the GUI component for a specific player and inventory configuration.
     * This method is responsible for rendering the component to the graphical user interface.
     *
     * @param viewer    The player for whom the component should be rendered.
     * @param inventory The inventory configuration used for rendering the GUI.
     */
    void render(@NotNull Player viewer, @NotNull ItemStack[][] inventory);

    /**
     * Set the GUI Engine API for the component.
     *
     * @param api The GuiEngineApi to set.
     */
    void setApi(@NotNull GuiEngineApi api);

    /**
     * Set the GUI context for the component.
     *
     * @param context The GuiContext to set.
     */
    void setContext(@NotNull GuiContext context);

    /**
     * Set the visibility of the GUI component.
     *
     * @param hidden true to hide the component, false to make it visible.
     */
    void setHidden(boolean hidden);

    /**
     * Set the X offset of the GUI component.
     *
     * @param x The X offset to set.
     */
    void setOffsetX(int x);

    /**
     * Set the Y offset of the GUI component.
     *
     * @param y The Y offset to set.
     */
    void setOffsetY(int y);

    /**
     * Serialize the GUI component using the provided GeneratorContext and SerializerProvider.
     * This method is used to serialize the component's properties to a JSON representation.
     *
     * @param gen         The GeneratorContext used for serialization.
     * @param serializers The SerializerProvider used for serialization.
     * @throws IOException If there is an error during serialization.
     */
    void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException;

    /**
     * Add actions to the GUI component.
     * This method allows adding GUI actions that can be triggered by the player interacting with the component.
     *
     * @param actions A set of actions to be added to the component.
     */
    default void addActions(@NotNull Set<Action> actions) {
    }

    /**
     * Check if the provided slot is within the bounds of the GUI component.
     * This method is used to determine if a player's interaction
     * (e.g., clicking a slot) is within the component's area.
     *
     * @param slot The slot index to check.
     * @return true if the slot is within the component's bounds, false otherwise.
     */
    default boolean isInComponent(int slot) {
        CoordinatePair pair = Utils.translateFromSlot(slot);
        int x = pair.x();
        int y = pair.y();

        return x >= offsetX() && x < offsetX() + width() && y >= offsetY() && y < offsetY() + height();
    }
}
