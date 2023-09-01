package io.github.toberocat.guiengine.interpreter.provided

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.provided.ChestGuiContext
import io.github.toberocat.guiengine.render.provided.ChestGuiRenderEngine
import io.github.toberocat.guiengine.xml.XmlGui
import org.bukkit.entity.Player

/**
 * DefaultInterpreter is an implementation of the GuiInterpreter interface that provides default
 * interpretation and handling of GUI components defined in XML format.
 *
 *
 * Created: 05/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
class ChestInterpreter : ContainerInterpreter {
    override val interpreterId = "chest"
    override val renderEngine = ChestGuiRenderEngine()
    override fun createContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui) = ChestGuiContext(
        this,
        xmlGui["title"].map { it.asText() }.orElse("Chest")
    )
}