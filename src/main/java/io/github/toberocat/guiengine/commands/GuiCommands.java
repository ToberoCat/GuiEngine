package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.toberocore.command.CommandExecutor;

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
        CommandExecutor executor = CommandExecutor.createExecutor("guiengine");
        executor.addChild(new OpenCommand());
        executor.addChild(new DumpCommand());
        executor.addChild(new GiveCommand(GuiEngineApiPlugin.getPlugin()));
        executor.addChild(new ReloadCommand());
    }
}
