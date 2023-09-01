package io.github.toberocat.guiengine.event.spigot

import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Represents a custom GUI engine event that extends the `Event` class from Bukkit.
 * This class is the base class for all GUI-related events in the GUI engine.
 *
 *
 * This class is licensed under the GNU General Public License.
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
open class GuiEngineEvent(val context: GuiContext) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        val handlerList = HandlerList()
    }
}
