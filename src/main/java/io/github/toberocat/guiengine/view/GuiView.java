package io.github.toberocat.guiengine.view;

import io.github.toberocat.guiengine.context.GuiContext;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * A record class representing a GUI view in a Minecraft plugin.
 */
public record GuiView(@NotNull Inventory inventory, @NotNull GuiContext context) {
}
