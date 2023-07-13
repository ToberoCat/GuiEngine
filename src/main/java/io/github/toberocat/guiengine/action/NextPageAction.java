package io.github.toberocat.guiengine.action;

import io.github.toberocat.guiengine.components.provided.paged.PagedComponent;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class NextPageAction extends Action {
    private final @NotNull PagedComponent pagedComponent;

    public NextPageAction(@NotNull PagedComponent pagedComponent) {
        this.pagedComponent = pagedComponent;
    }

    @Override
    public @NotNull String label() {
        return pagedComponent.getId() + ":next";
    }

    @Override
    public void run(@NotNull Player player) {
        int value = pagedComponent.getShowingPage() + 1;
        if (value >= pagedComponent.getAvailablePages())
            return;
        pagedComponent.setShowingPage(value);
    }
}
