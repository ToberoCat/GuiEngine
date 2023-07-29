package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.toberocore.command.exceptions.CommandExceptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Executes the `DumpCommand` for the specified player with the provided arguments.
     *
     * @param player The player executing the command.
     * @param args   The arguments passed to the command.
     * @return `true` if the command execution is successful, `false` otherwise.
     * @throws CommandExceptions If an error occurs during command execution.
     */
    @Override
    protected boolean runPlayer(@NotNull Player player, @NotNull String @NotNull [] args) throws CommandExceptions {
        if (0 == args.length) throw new CommandExceptions("This command needs a GUI ID provided");

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
            throw new CommandExceptions(e.getMessage());
        }
        return true;
    }
}
