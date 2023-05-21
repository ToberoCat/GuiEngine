package io.github.toberocat.guiengine.api.view;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Created: 06.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiViewManager {
    @Nullable GuiView getGuiView(@NotNull UUID player);
    boolean isViewingGui(@NotNull UUID player);

    void registerGui(@NotNull UUID player, @NotNull GuiView guiView);

    default @Nullable GuiView getGuiView(@NotNull Player player) {
        return getGuiView(player.getUniqueId());
    }

    default boolean isViewingGui(@NotNull Player player) {
        return isViewingGui(player.getUniqueId());
    }
}
