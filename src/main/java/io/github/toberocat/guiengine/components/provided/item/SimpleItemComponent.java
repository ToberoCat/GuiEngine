package io.github.toberocat.guiengine.components.provided.item;

import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.JsonUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * A GUI component representing a simple item in the GUI.
 * This component displays a single item at a specific position in the GUI.
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public class SimpleItemComponent extends AbstractGuiComponent {
    public static final @NotNull String TYPE = "item";
    protected final @NotNull ItemStack stack;

    /**
     * Constructor for SimpleItemComponent.
     *
     * @param offsetX        The X offset of the GUI component.
     * @param offsetY        The Y offset of the GUI component.
     * @param priority       The rendering priority of the GUI component.
     * @param id             The ID of the GUI component.
     * @param clickFunctions The list of click functions for the GUI component.
     * @param dragFunctions  The list of drag functions for the GUI component.
     * @param closeFunctions The list of close functions for the GUI component.
     * @param stack          The ItemStack to be displayed in the GUI component.
     * @param hidden         true if the GUI component is hidden, false otherwise.
     */
    public SimpleItemComponent(int offsetX, int offsetY, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, @NotNull ItemStack stack, boolean hidden) {
        super(offsetX, offsetY, 1, 1, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden);
        this.stack = stack;
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        gen.writeStringField("material", stack.getType().name());

        ItemMeta meta = stack.getItemMeta();
        if (null == meta) return;

        gen.writeStringField("name", meta.getDisplayName());
        if (null != meta.getLore()) JsonUtils.writeArray(gen, "lore", meta.getLore().toArray());

        if (!(meta instanceof SkullMeta skullMeta)) return;

        if (null == skullMeta.getOwningPlayer()) return;

        gen.writeStringField("head-owner", skullMeta.getOwningPlayer().getUniqueId().toString());
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer, @NotNull ItemStack[][] inventory) {
        if (null == context || null == api) return;
        inventory[offsetY][offsetX] = stack;
    }
}
