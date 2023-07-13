package io.github.toberocat.guiengine.api.action;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class OpenGuiAction extends Action {

    @Override
    public @NotNull String label() {
        return "open";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args) {
        if (args.length != 2)
            return;

        String apiId = args[0];
        String guiID = args[1];
        GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
        if (api == null)
            return;

        api.openGui(player, guiID);
    }
}
