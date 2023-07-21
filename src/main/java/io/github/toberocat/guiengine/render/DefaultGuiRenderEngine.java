package io.github.toberocat.guiengine.render;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.view.DefaultGuiViewManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * DefaultGuiRenderEngine is an implementation of the GuiRenderEngine interface responsible for rendering GUIs.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public class DefaultGuiRenderEngine implements GuiRenderEngine {

    /**
     * The GuiViewManager associated with this render engine.
     */
    protected final DefaultGuiViewManager viewManager;

    /**
     * Creates a new DefaultGuiRenderEngine instance and initializes it with the GuiViewManager from the plugin.
     */
    public DefaultGuiRenderEngine() {
        this.viewManager = GuiEngineApiPlugin.getPlugin().getGuiViewManager();
    }

    /**
     * Retrieves the GuiViewManager associated with this render engine.
     *
     * @return The GuiViewManager instance.
     */
    @Override
    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return viewManager;
    }

    /**
     * Renders the GUI using the provided render buffer, GuiContext, and Player viewer.
     *
     * @param renderBuffer The 2D array of ItemStacks representing the render buffer.
     * @param context      The GuiContext to be rendered.
     * @param viewer       The Player who will view the rendered GUI.
     */
    @Override
    public void renderGui(@NotNull ItemStack[][] renderBuffer, @NotNull GuiContext context, @NotNull Player viewer) {
        context.componentsAscending().filter(x -> !x.hidden()).forEachOrdered(x -> x.render(viewer, renderBuffer));
    }
}
