package io.github.toberocat.guiengine.api.components.provided.embedded;

import io.github.toberocat.guiengine.api.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.VirtualInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


/**
 * Created: 06.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class EmbeddedGuiComponent extends AbstractGuiComponent {
    public static final @NotNull String TYPE = "embedded";
    protected final @NotNull String targetGui;
    protected final boolean copyAir, interactions;

    protected @Nullable GuiContext embedded;

    public EmbeddedGuiComponent(@NotNull String id,
                                int x,
                                int y,
                                int width,
                                int height,
                                @NotNull RenderPriority priority,
                                @NotNull String targetGui,
                                boolean copyAir,
                                boolean interactions) {
        super(id, x, y, width, height, priority);
        this.targetGui = targetGui;
        this.copyAir = copyAir;
        this.interactions = interactions;
    }

    @Override
    public void onViewInit(@NotNull Map<String, String> placeholders) {
        if (context == null || api == null || context.viewer() == null)
            return;

        embedded = context.interpreter().loadContent(api, context.viewer(),
                api.loadXmlGui(placeholders, targetGui));
        embedded.setInventory(new VirtualInventory(height, () -> context.render()));
        embedded.setViewer(context.viewer());
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        if (!interactions || embedded == null)
            return;
        embedded.clickedComponent(event);
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        if (!interactions || embedded == null)
            return;
        embedded.draggedComponent(event);
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        if (!interactions || embedded == null)
            return;
        embedded.closedComponent(event);
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer,
                       @NotNull ItemStack[][] buffer) {
        if (context == null || api == null || embedded == null)
            return;

        ItemStack[][] virtualInventory = new ItemStack[embedded.height()][embedded.width()];
        context.interpreter().getRenderEngine().renderGui(virtualInventory, embedded, viewer);

        int width = Math.min(context.width() - offsetX, embedded.width());
        for (int y = 0; y < Math.min(context.height() - offsetY, embedded.height()); y++) {
            for (int x = 0; x < width; x++) {
                ItemStack item = virtualInventory[y][x];
                if (copyAir || (item != null && item.getType() != Material.AIR))
                    buffer[y + offsetY][x + offsetX] = item;
            }
        }
    }
}
