package io.github.toberocat.guiengine.api.components.provided.head;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;


/**
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
// ToDo: Merge with item type
public class HeadItemComponent extends SimpleItemComponent {
    public static final @NotNull String TYPE = "head";

    public HeadItemComponent(int offsetX, int offsetY, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, @NotNull ItemStack stack, boolean hidden) {
        super(offsetX, offsetY, priority, id, clickFunctions, dragFunctions, closeFunctions, stack, hidden);
    }

    @Override
    public void serialize(@NotNull JsonGenerator gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);

        if (!(stack.getItemMeta() instanceof SkullMeta skullMeta))
            return;

        //ToDo: Add the head texture field
        if (skullMeta.getOwningPlayer() == null)
            return;

        gen.writeStringField("head-owner", skullMeta.getOwningPlayer().getUniqueId()
                .toString());

    }
}
