package io.github.toberocat.guiengine.action;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * The `OpenGuiAction` class represents an action that allows a player to open a GUI using the `GuiEngineApi`.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 29.04.2023
 */
public class OpenGuiAction extends Action {

    /**
     * Returns the label of this action, which is "open".
     *
     * @return The label of this action.
     */
    @Override
    public @NotNull String label() {
        return "open";
    }

    /**
     * Performs the action of opening a GUI for the specified player using the `GuiEngineApi`.
     *
     * @param player The player who triggered the action.
     * @param args   The arguments passed to the action. The first argument is expected to be the API ID, and the second argument is the GUI ID.
     */
    @Override
    public void run(@NotNull Player player, @NotNull String[] args) {
        if (args.length != 2) return;

        String apiId = args[0];
        String guiId = args[1];
        GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
        if (api == null) return;

        try {
            api.openGui(player, guiId);
        } catch (Exception e) {
            GuiEngineApiPlugin.getPlugin().getLogger().log(Level.WARNING, e.getMessage());
        }
    }
}
