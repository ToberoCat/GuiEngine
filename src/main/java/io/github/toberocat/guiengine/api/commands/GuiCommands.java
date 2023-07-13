package io.github.toberocat.guiengine.api.commands;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.exception.GuiIORuntimeException;
import io.github.toberocat.toberocore.command.CommandExecutor;
import io.github.toberocat.toberocore.command.subcommands.QuickSubCommand;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 05/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiCommands {
    public GuiCommands() {
        CommandExecutor executor = CommandExecutor.createExecutor("", "guiengine");
        executor.addChild(new OpenCommand());
        executor.addChild(new QuickSubCommand("reload", (s, a) -> {
            try {
                for (GuiEngineApi api : GuiEngineApi.APIS.values())
                    api.reload();
                s.sendMessage("§aReloaded gui apis");
            } catch (GuiIORuntimeException e) {
                s.sendMessage(e.getMessage());
            }
            return true;
        }));
    }
}
