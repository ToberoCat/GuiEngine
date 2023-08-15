package io.github.toberocat.guiengine.commands;

import io.github.toberocat.toberocore.command.SubCommand;
import io.github.toberocat.toberocore.command.arguments.Argument;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import io.github.toberocat.toberocore.command.options.Options;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WebEditorCommand extends SubCommand {
    public WebEditorCommand() {
        super("webeditor");
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
    protected boolean handleCommand(@NotNull CommandSender sender,
                                    @NotNull String[] strings) throws CommandException {
        sender.sendMessage("§6The Gui web editor is only available for gui engine premium users");
        return true;
    }
}
