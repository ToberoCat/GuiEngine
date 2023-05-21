package io.github.toberocat.guiengine.api.components.provided.head;

import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class HeadItemComponent extends SimpleItemComponent {
    public static final @NotNull String TYPE = "head";

    public HeadItemComponent(@NotNull RenderPriority priority,
                             @NotNull String id,
                             @NotNull ItemStack item,
                             @NotNull List<GuiFunction> clickFunctions,
                             int x,
                             int y) {
        super(priority, id, item, clickFunctions, x, y);
    }
}
