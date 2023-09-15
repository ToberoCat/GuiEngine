package io.github.toberocat.guiengine.function

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.guiengine.context.GuiContext
import org.bukkit.Bukkit
import java.util.function.BiConsumer

/**
 * Represents a function that can be called on a GUI context.
 *
 *
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface GuiFunction {
    val type: String

    /**
     * Calls the GUI function with the specified API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context on which the function is called.
     */
    fun call(api: GuiEngineApi, context: GuiContext)

    companion object {
        fun anonymous(method: BiConsumer<GuiEngineApi, GuiContext>): GuiFunction = object : GuiFunction {
            override val type = "anonymous"

            override fun call(api: GuiEngineApi, context: GuiContext) {
                method.accept(api, context)
            }
        }

        fun anonymousSync(method: BiConsumer<GuiEngineApi, GuiContext>): GuiFunction = object : GuiFunction {
            override val type = "anonymousSync"

            override fun call(api: GuiEngineApi, context: GuiContext) {
                Bukkit.getScheduler().runTask(GuiEngineApiPlugin.plugin, Runnable {
                    method.accept(api, context)
                })
            }
        }
    }
}
