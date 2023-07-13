package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.toberocore.command.CommandExecutor;
import io.github.toberocat.toberocore.command.subcommands.QuickSubCommand;

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
