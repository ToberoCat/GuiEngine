package io.github.toberocat.guiengine.components.provided.embedded;

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
 * A GUI component that embeds another GUI inside it.
 * This component allows embedding and rendering a target GUI inside the current GUI.
 * Created: 06.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class EmbeddedGuiComponent extends AbstractGuiComponent {
    public static final @NotNull String TYPE = "embedded";
    protected final @NotNull String targetGui;
    protected final boolean copyAir, interactions;

    protected @Nullable GuiContext embedded;

    /**
     * Constructor for EmbeddedGuiComponent.
     *
     * @param offsetX        The X offset of the GUI component.
     * @param offsetY        The Y offset of the GUI component.
     * @param width          The width of the GUI component.
     * @param height         The height of the GUI component.
     * @param priority       The rendering priority of the GUI component.
     * @param id             The ID of the GUI component.
     * @param clickFunctions The list of click functions for the GUI component.
     * @param dragFunctions  The list of drag functions for the GUI component.
     * @param closeFunctions The list of close functions for the GUI component.
     * @param hidden         true if the GUI component is hidden, false otherwise.
     * @param targetGui      The ID of the target GUI to embed inside this component.
     * @param copyAir        true to copy air slots from the embedded GUI, false to skip empty slots.
     * @param interactions   true to allow interactions with the embedded GUI, false to ignore interactions.
     */
    public EmbeddedGuiComponent(int offsetX, int offsetY, int width, int height, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, boolean hidden, @NotNull String targetGui, boolean copyAir, boolean interactions) {
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
        if (null == context || null == api || null == context.viewer()) return;

        embedded = context.interpreter().loadContent(api, context.viewer(), api.loadXmlGui(placeholders, targetGui));
        embedded.setInventory(new VirtualInventory(height, () -> context.render()));
        embedded.setViewer(context.viewer());
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        super.clickedComponent(event);
        if (!interactions || null == embedded) return;
        int fakeHotBarButton = event.getHotbarButton();
        if (fakeHotBarButton > 0) {
            fakeHotBarButton = (fakeHotBarButton * 10) * 10 + offsetY;
        } else if (fakeHotBarButton == 0) {
            fakeHotBarButton = 1000 + offsetY;
        } else {
            fakeHotBarButton = (Math.abs(fakeHotBarButton) * 10 + 1) * 10 + offsetY;
        }
        InventoryClickEvent fakedEvent = new InventoryClickEvent(event.getView(), event.getSlotType(), event.getSlot() - Utils.translateToSlot(offsetX, offsetY), event.getClick(), event.getAction(), fakeHotBarButton);
        embedded.clickedComponent(fakedEvent);
    }

    @Override
    public void draggedComponent(@NotNull InventoryDragEvent event) {
        super.draggedComponent(event);
        if (!interactions || null == embedded) return;
        // ToDo: Fake the event (offset the slot to be originated at zero)
        embedded.draggedComponent(event);
    }

    @Override
    public void closedComponent(@NotNull InventoryCloseEvent event) {
        super.closedComponent(event);
        if (!interactions || null == embedded) return;
        embedded.closedComponent(event);
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer, @NotNull ItemStack[][] buffer) {
        if (null == context || null == api || null == embedded) return;

        ItemStack[][] virtualInventory = new ItemStack[embedded.height()][embedded.width()];
        context.interpreter().getRenderEngine().renderGui(virtualInventory, embedded, viewer);

        int width = Math.min(context.width() - offsetX, embedded.width());
        for (int y = 0; y < Math.min(context.height() - offsetY, embedded.height()); y++) {
            for (int x = 0; x < width; x++) {
                ItemStack item = virtualInventory[y][x];
                if (copyAir || (null != item && Material.AIR != item.getType()))
                    buffer[y + offsetY][x + offsetX] = item;
            }
        }
    }
}
