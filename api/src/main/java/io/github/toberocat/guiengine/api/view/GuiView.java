package io.github.toberocat.guiengine.api.view;

import io.github.toberocat.guiengine.api.context.GuiContext;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public record GuiView(@NotNull Inventory inventory, @NotNull GuiContext context) {
}
