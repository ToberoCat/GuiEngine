package io.github.toberocat.guiengine.action

import io.github.toberocat.guiengine.components.provided.paged.PagedComponent
import io.github.toberocat.guiengine.utils.GuiEngineAction
import org.bukkit.entity.Player

/**
 * The `PreviousPageAction` class represents an action that allows a player to navigate to the previous page of a
 * `PagedComponent`.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 30.04.2023
 */
class PreviousPageAction(private val pagedComponent: PagedComponent) : GuiEngineAction() {
    /**
     * Returns the label of this action, which is based on the ID of the associated `PagedComponent`
     * and the action type ("previous").
     *
     * @return The label of this action.
     */
    override fun label(): String = pagedComponent.id + ":previous"

    /**
     * Performs the action of navigating to the previous page of the associated `PagedComponent`
     * for the specified player.
     *
     * @param player The player who triggered the action.
     */
    override fun run(player: Player) {
        val value = (pagedComponent.getShowingPage() - 1) % pagedComponent.availablePages
        if (0 > value) return
        pagedComponent.setShowingPage(value)
    }
}
