package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.toberocore.command.exceptions.CommandExceptions;
import io.github.toberocat.toberocore.command.subcommands.PlayerSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * The `OpenCommand` class represents a player sub-command that allows a player to open a GUI using the `GuiEngineApi`.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 07.04.2023
 */
public class OpenCommand extends PlayerSubCommand {
    /**
     * Constructs a new `OpenCommand` with the default label "open".
     */
    public OpenCommand() {
        this("open");
    }

    /**
     * Constructs a new `OpenCommand` with the specified label.
     *
     * @param label The label for this command.
     */
    public OpenCommand(@NotNull String label) {
        super(label);
    }

    /**
     * Executes the `OpenCommand` for the specified player with the provided arguments.
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

            api.openGui(player, guiId);
        } catch (GuiNotFoundRuntimeException | GuiIORuntimeException e) {
            throw new CommandExceptions(e.getMessage());
        }
        return true;
    }

    /**
     * Returns a list of available API IDs or GUI IDs for tab completion based on the provided arguments.
     *
     * @param player The player executing the command.
     * @param args   The arguments passed to the command.
     * @return A list of available API IDs or GUI IDs for tab completion.
     */
    @Override
    protected @Nullable List<String> runPlayerTab(@NotNull Player player, @NotNull String @NotNull [] args) {
        if (1 >= args.length) return GuiEngineApi.APIS.keySet().stream().toList();
        GuiEngineApi api = GuiEngineApi.APIS.get(args[0]);
        if (null == api) {
            player.sendMessage("§cNo API found with ID " + args[0]);
            return Collections.emptyList();
        }

        return api.getAvailableGuis().stream().toList();
    }
}
