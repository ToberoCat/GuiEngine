package io.github.toberocat.guiengine.interpreter.provided

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.provided.TitleContext
import io.github.toberocat.guiengine.render.provided.FurnaceGuiRenderEngine
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
class FurnaceInterpreter : ContainerInterpreter {
    override val interpreterId = "furnace"
    override val renderEngine = FurnaceGuiRenderEngine()
    override fun createContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui) = TitleContext(
        this, xmlGui["title"].map { it.asText() }.orElse("GuiEngine Gui")
    )
}