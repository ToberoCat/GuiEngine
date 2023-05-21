package io.github.toberocat.guiengine.api.components.provided.item;

import io.github.toberocat.guiengine.api.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.api.function.FunctionProcessor;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class SimpleItemComponent extends AbstractGuiComponent {
    public static final @NotNull String TYPE = "item";
    protected final ItemStack itemStack;
    protected final List<GuiFunction> clickFunctions;

    public SimpleItemComponent(@NotNull RenderPriority priority,
                               @NotNull String id,
                               @NotNull ItemStack stack,
                               @NotNull List<GuiFunction> clickFunctions,
                               int x,
                               int y) {
        super(id, x, y, 1, 1, priority);
        this.itemStack = stack;
        this.clickFunctions = clickFunctions;
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        if (context == null || api == null)
            return;

        FunctionProcessor.callFunctions(clickFunctions, api, context);
        context.render();
    }

    @Override
    public void render(@NotNull Player viewer,
                       @NotNull ItemStack[][] inventory) {
        if (context == null || api == null)
            return;
        inventory[offsetY][offsetX] = Utils.processItemStack(itemStack, api, context);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<GuiFunction> getClickFunctions() {
        return clickFunctions;
    }

    @Override
    public boolean isInComponent(int slot) {
        return super.isInComponent(slot);
    }
}
