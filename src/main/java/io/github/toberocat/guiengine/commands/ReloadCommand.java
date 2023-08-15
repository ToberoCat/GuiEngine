package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.toberocore.command.SubCommand;
import io.github.toberocat.toberocore.command.arguments.Argument;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import io.github.toberocat.toberocore.command.options.Options;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super("reload");
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
    protected boolean handleCommand(@NotNull CommandSender sender, @NotNull String[] strings) throws CommandException {
        try {
            GuiEngineApiPlugin.getPlugin().reloadConfig();

            for (GuiEngineApi api : GuiEngineApi.APIS.values())
                api.reload();
            sender.sendMessage("§aReloaded GUI APIs.");
        } catch (GuiIORuntimeException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }
}
