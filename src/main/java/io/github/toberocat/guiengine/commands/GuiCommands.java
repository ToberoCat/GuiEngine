package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.toberocore.command.CommandExecutor;
import io.github.toberocat.toberocore.command.subcommands.QuickSubCommand;

/**
 * The `GuiCommands` class sets up and registers GUI-related commands for the `GuiEngineApi`.
 * <p>
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 05/02/2023
 */
public class GuiCommands {
    /**
     * Constructs a new `GuiCommands` and sets up the GUI-related commands.
     */
    public GuiCommands() {
        CommandExecutor executor = CommandExecutor.createExecutor("", "guiengine");
        executor.addChild(new OpenCommand());
        executor.addChild(new DumpCommand());
        executor.addChild(new GiveCommand(GuiEngineApiPlugin.getPlugin()));
        executor.addChild(new QuickSubCommand("reload", (sender, args) -> {
            try {
                GuiEngineApiPlugin.getPlugin().reloadConfig();

                for (GuiEngineApi api : GuiEngineApi.APIS.values())
                    api.reload();
                sender.sendMessage("§aReloaded GUI APIs.");
            } catch (GuiIORuntimeException e) {
                sender.sendMessage(e.getMessage());
            }
            return true;
        }));
    }
}
