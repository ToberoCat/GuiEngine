package io.github.toberocat.guiengine.commands

import io.github.toberocat.guiengine.GuiEngineApiPlugin.Companion.plugin
import io.github.toberocat.toberocore.command.CommandExecutor

/**
 * The `GuiCommands` class sets up and registers GUI-related commands for the `GuiEngineApi`.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 05/02/2023
 */
class GuiCommands {
    /**
     * Constructs a new `GuiCommands` and sets up the GUI-related commands.
     */
    init {
        val executor = CommandExecutor.createExecutor("guiengine")
        executor.addChild(OpenCommand())
        executor.addChild(DumpCommand())
        executor.addChild(GiveCommand(plugin))
        executor.addChild(ReloadCommand())
        executor.addChild(WebEditorCommand())
    }
}
