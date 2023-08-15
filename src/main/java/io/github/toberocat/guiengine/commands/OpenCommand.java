package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.toberocore.command.PlayerSubCommand;
import io.github.toberocat.toberocore.command.arguments.Argument;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import io.github.toberocat.toberocore.command.options.Options;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
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

    @Override
    protected @NotNull Options options() {
        return new Options();
    }

    @Override
    protected @NotNull Argument<?>[] arguments() {
        return new Argument[0];
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

            api.openGui(player, guiId);
        } catch (GuiNotFoundRuntimeException | GuiIORuntimeException e) {
            throw new CommandException(e.getMessage(), new HashMap<>());
        }
        return true;
    }

    @Override
    public @Nullable List<String> routeTab(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!sender.hasPermission(this.getPermission())) {
            return null;
        }

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        if (1 >= newArgs.length) return GuiEngineApi.APIS.keySet().stream().toList();
        GuiEngineApi api = GuiEngineApi.APIS.get(newArgs[0]);
        if (null == api) {
            sender.sendMessage("§cNo API found with ID " + newArgs[0]);
            return Collections.emptyList();
        }

        return api.getAvailableGuis().stream().toList();
    }
}
