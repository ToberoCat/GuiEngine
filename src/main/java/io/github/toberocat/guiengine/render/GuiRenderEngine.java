package io.github.toberocat.guiengine.render;

import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.view.GuiView;
import io.github.toberocat.guiengine.view.GuiViewManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * GuiRenderEngine is an interface for rendering GUIs and managing their views.
 * <p>
 * Created: 06.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public interface GuiRenderEngine {

    /**
     * Retrieves the GuiViewManager associated with this render engine.
     *
     * @return The GuiViewManager instance.
     */
    @NotNull
    GuiViewManager getGuiViewManager();

    /**
     * Renders the GUI using the provided render buffer, GuiContext, and Player viewer.
     *
     * @param renderBuffer The 2D array of ItemStacks representing the render buffer.
     * @param context      The GuiContext to be rendered.
     * @param viewer       The Player who will view the rendered GUI.
     */
    void renderGui(@NotNull ItemStack[][] renderBuffer,
                   @NotNull GuiContext context,
                   @NotNull Player viewer);

    /**
     * Shows the GUI to the specified player using the provided GuiContext and placeholders.
     *
     * @param content      The GuiContext representing the GUI content to be displayed.
     * @param viewer       The Player who will view the GUI.
     * @param placeholders A map of placeholders used for dynamic content in the GUI.
     */
    default void showGui(@NotNull GuiContext content,
                         @NotNull Player viewer,
                         @NotNull Map<String, String> placeholders) {
        Inventory inventory = createInventory(content, viewer, placeholders);

        getGuiViewManager().registerGui(viewer.getUniqueId(), new GuiView(inventory, content));
        viewer.openInventory(inventory);
    }

    /**
     * Creates an inventory representing the GUI using the provided GuiContext and placeholders.
     *
     * @param context      The GuiContext representing the GUI content.
     * @param viewer       The Player who will view the GUI.
     * @param placeholders A map of placeholders used for dynamic content in the GUI.
     * @return The created Inventory representing the GUI.
     */
    default @NotNull Inventory createInventory(@NotNull GuiContext context,
                                               @NotNull Player viewer,
                                               @NotNull Map<String, String> placeholders) {
        Inventory inventory = Bukkit.createInventory(viewer, context.height() * 9, context.title());
        context.setInventory(inventory);

        context.componentsDescending().forEach(event -> event.onViewInit(placeholders));
        context.render();
        return inventory;
    }
}
