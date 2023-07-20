package io.github.toberocat.guiengine.components.provided.embedded;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.Utils;
import io.github.toberocat.guiengine.utils.VirtualInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
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

    public EmbeddedGuiComponent(int offsetX,
                                int offsetY,
                                int width,
                                int height,
                                @NotNull RenderPriority priority,
                                @NotNull String id,
                                @NotNull List<GuiFunction> clickFunctions,
                                @NotNull List<GuiFunction> dragFunctions,
                                @NotNull List<GuiFunction> closeFunctions,
                                boolean hidden,
                                @NotNull String targetGui,
                                boolean copyAir,
                                boolean interactions) {
        super(offsetX, offsetY, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden);
        this.targetGui = targetGui;
        this.copyAir = copyAir;
        this.interactions = interactions;
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        gen.writeStringField("target-gui", targetGui);
        gen.writeBooleanField("copy-air", copyAir);
        gen.writeBooleanField("interactions", interactions);
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
        super.clickedComponent(event);
        if (!interactions || embedded == null)
            return;
        InventoryClickEvent fakedEvent = new InventoryClickEvent(event.getView(),
                event.getSlotType(),
                event.getSlot() - Utils.translateToSlot(offsetX, offsetY),
                event.getClick(),
                event.getAction(),
                event.getHotbarButton());
        embedded.clickedComponent(fakedEvent);
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        super.draggedComponent(event);
        if (!interactions || embedded == null)
            return;
        // ToDo: Fake the event (offset the slot to be originated at zero)
        embedded.draggedComponent(event);
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        super.closedComponent(event);
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
