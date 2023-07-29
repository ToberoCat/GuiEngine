package io.github.toberocat.guiengine.action;

import io.github.toberocat.guiengine.components.provided.paged.PagedComponent;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The `PreviousPageAction` class represents an action that allows a player to navigate to the previous page of a
 * `PagedComponent`.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 30.04.2023
 */
public class PreviousPageAction extends Action {
    private final @NotNull PagedComponent pagedComponent;

    /**
     * Constructs a new `PreviousPageAction` with the specified `PagedComponent`.
     *
     * @param pagedComponent The `PagedComponent` for which this action is associated.
     */
    public PreviousPageAction(@NotNull PagedComponent pagedComponent) {
        this.pagedComponent = pagedComponent;
    }

    /**
     * Returns the label of this action, which is based on the ID of the associated `PagedComponent`
     * and the action type ("previous").
     *
     * @return The label of this action.
     */
    @Override
    public @NotNull String label() {
        return pagedComponent.getId() + ":previous";
    }

    /**
     * Performs the action of navigating to the previous page of the associated `PagedComponent`
     * for the specified player.
     *
     * @param player The player who triggered the action.
     */
    @Override
    public void run(@NotNull Player player) {
        int value = (pagedComponent.getShowingPage() - 1) % pagedComponent.getAvailablePages();
        if (0 > value) return;
        pagedComponent.setShowingPage(value);
    }
}
