package io.github.toberocat.guiengine.event.spigot

import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.event.Event

/**
 * Represents a custom GUI engine event that extends the `Event` class from Bukkit.
 * This class is the base class for all GUI-related events in the GUI engine.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
abstract class GuiEngineEvent(val context: GuiContext) : Event()