package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException;
import io.github.toberocat.toberocore.command.exceptions.CommandExceptions;
import io.github.toberocat.toberocore.command.subcommands.PlayerSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created: 14.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class DumpCommand extends OpenCommand {
    public DumpCommand() {
        super("dump-open");
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
            GuiContext context = api.openGui(player, args[1]);
            GuiEngineApiPlugin.getPlugin().getLogger().info(String.valueOf(context));
        } catch (GuiNotFoundRuntimeException | GuiIORuntimeException e) {
            throw new CommandExceptions(e.getMessage());
        }
        return true;
    }
}
