package io.github.toberocat.guiengine.render;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.view.DefaultGuiViewManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class DefaultGuiRenderEngine implements GuiRenderEngine {

    protected final DefaultGuiViewManager viewManager;

    public DefaultGuiRenderEngine() {
        this.viewManager = GuiEngineApiPlugin.getPlugin().getGuiViewManager();
    }

    @Override
    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return viewManager;
    }

    @Override
    public void renderGui(@NotNull ItemStack[][] renderBuffer,
                          @NotNull GuiContext context,
                          @NotNull Player viewer) {
        context.componentsAscending()
                .filter(x -> !x.hidden())
                .forEachOrdered(x -> x.render(viewer, renderBuffer));
    }
}
