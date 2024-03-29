package io.github.toberocat.guiengine.interpreter.provided

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.ContextType
import io.github.toberocat.guiengine.context.provided.SizeableGuiContext
import io.github.toberocat.guiengine.render.provided.SizeableGuiRenderEngine
import io.github.toberocat.guiengine.xml.XmlGui
import org.bukkit.entity.Player

class SizeableInterpreter : ContainerInterpreter {
    override val interpreterId = "default"
    override val renderEngine = SizeableGuiRenderEngine()

    override fun containerContext(api: GuiEngineApi, viewer: Player, xmlGui: XmlGui) = SizeableGuiContext(
        this,
        xmlGui,
        api,
        xmlGui["title"].map { it.asText() }.orElse("GuiEngine Gui"),
        xmlGui["width"].map { it.asInt() }.orElse(9),
        xmlGui["height"].map { it.asInt() }.orElse(3),
        ContextType.STATIC
    )
}