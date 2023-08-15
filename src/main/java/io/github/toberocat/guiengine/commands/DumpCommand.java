package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * The `DumpCommand` class represents a player sub-command that allows a player to open a GUI and dump its context.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 14.07.2023
 */
public class DumpCommand extends OpenCommand {
    /**
     * Constructs a new `DumpCommand`.
     */
    public DumpCommand() {
        super("dump-open");
    }

    @Override
    protected boolean handle(@NotNull Player player, @NotNull String[] args) throws CommandException {
        if (0 == args.length) throw new CommandException("This command needs a GUI ID provided", new HashMap<>());

        try {
            String apiId = args[0];
            String guiId = args[1];

            GuiEngineApi api = GuiEngineApi.APIS.get(apiId);
            if (null == api) {
                player.sendMessage("§cNo API found with ID " + apiId);
                return false;
            }

            GuiContext context = api.openGui(player, guiId);
            GuiEngineApiPlugin.getPlugin().getLogger().info(String.valueOf(context));
        } catch (GuiNotFoundRuntimeException | GuiIORuntimeException e) {
            throw new CommandException(e.getMessage(), new HashMap<>());
        }
        return true;
    }
}
