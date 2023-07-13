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
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class OpenCommand extends PlayerSubCommand {
    public OpenCommand() {
        super("open");
    }

    @Override
    protected boolean runPlayer(@NotNull Player player, @NotNull String[] args) throws CommandExceptions {
        if (args.length == 0)
            throw new CommandExceptions("This command needs a gui provided");

        try {
            GuiEngineApi api = GuiEngineApi.APIS.get(args[0]);
            if (api == null) {
                player.sendMessage("§cNo API found with id " + args[0]);
                return false;
            }
            api.openGui(player, args[1]);
        } catch (GuiNotFoundRuntimeException | GuiIORuntimeException e) {
            throw new CommandExceptions(e.getMessage());
        }
        return true;
    }

    @Override
    protected @Nullable List<String> runPlayerTab(@NotNull Player player, @NotNull String[] args) {
        if (args.length <= 1)
            return GuiEngineApi.APIS.keySet().stream().toList();
        GuiEngineApi api = GuiEngineApi.APIS.get(args[0]);
        if (api == null) {
            player.sendMessage("§cNo API found with id " + args[0]);
            return Collections.emptyList();
        }

        return api.getAvailableGuis().stream().toList();
    }
}
