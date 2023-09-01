package io.github.toberocat.guiengine

import io.github.toberocat.guiengine.action.OpenGuiAction
import io.github.toberocat.guiengine.commands.GuiCommands
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponentBuilder
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder
import io.github.toberocat.guiengine.components.provided.paged.PagedComponent
import io.github.toberocat.guiengine.components.provided.paged.PagedComponentBuilder
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponent
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponentBuilder
import io.github.toberocat.guiengine.exception.GuiIORuntimeException
import io.github.toberocat.guiengine.function.FunctionProcessor
import io.github.toberocat.guiengine.function.call.*
import io.github.toberocat.guiengine.function.compute.GuiComponentPropertyFunction
import io.github.toberocat.guiengine.function.compute.HasNotPermissionFunction
import io.github.toberocat.guiengine.function.compute.HasPermissionFunction
import io.github.toberocat.guiengine.interpreter.InterpreterManager
import io.github.toberocat.guiengine.interpreter.provided.ChestInterpreter
import io.github.toberocat.guiengine.interpreter.provided.SizeableInterpreter
import io.github.toberocat.guiengine.item.GuiItemManager
import io.github.toberocat.guiengine.listeners.ItemClickListener
import io.github.toberocat.guiengine.listeners.PlayerJoinListener
import io.github.toberocat.guiengine.utils.BStatsCollector
import io.github.toberocat.guiengine.utils.UpdateChecker
import io.github.toberocat.guiengine.utils.Utils
import io.github.toberocat.guiengine.view.DefaultGuiViewManager
import io.github.toberocat.toberocore.action.ActionCore
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * This class represents the main plugin class for the GuiEngineApi plugin.
 * It extends JavaPlugin and acts as the entry point for the plugin's functionality.
 * The plugin provides a graphical user interface (GUI) engine for creating and managing custom GUIs in Minecraft.
 * It supports various components like SimpleItemComponent, EmbeddedGuiComponent, ToggleItemComponent, and PagedComponent.
 * Additionally, it allows the registration of functions and actions for GUI interactions.
 * The plugin is licensed under the GNU GENERAL PUBLIC LICENSE.
 * For usage and documentation, refer to the README or the official documentation of the GuiEngineApi.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 04/02/2023
 */
class GuiEngineApiPlugin : JavaPlugin() {
    /**
     * The manager responsible for handling GUI interpreters.
     */
    private var interpreterManager: InterpreterManager? = null

    /**
     * The manager responsible for managing GUI views.
     */
    private var guiViewManager: DefaultGuiViewManager? = null

    /**
     * Gets the item manager
     *
     * @return The GuiItem Manager
     */
    var guiItemManager: GuiItemManager? = null
        private set
    var guiApi: GuiEngineApi? = null
        private set

    /**
     * Called when the plugin is enabled. Initializes the plugin and registers necessary components, functions, actions, and commands.
     * Creates and initializes the manager instances for GUI views and interpreters.
     * Registers default shared factories for supported GUI components.
     * Register default functions for GUI interactions, such as adding, editing, or removing components.
     * Register default compute functions for evaluating GUI component properties and permissions.
     * Adds a default API instance for handling GUI data.
     * Register default actions for GUI interactions.
     */
    override fun onEnable() {
        saveDefaultConfig()
        instance = this
        createManagers()
        registerListeners()
        registerInterpreters()
        registerComponents()
        registerFunctions()
        registerActions()
        addDefaultApi()
        registerCommands()
        checkForUpdate()
        BStatsCollector(this)
    }

    override fun onDisable() {
        logger.info("GuiEngine is shutting down it's threads...")
        Utils.threadPool.shutdown()
        try {
            if (Utils.threadPool.awaitTermination(
                    10,
                    TimeUnit.SECONDS
                )
            ) logger.info("All threads have been stopped. Continuing with server shutdown") else logger.warning("Shutting down the threads took longer than 10 seconds. Skipping shutdown. Might need a server restart")
        } catch (e: InterruptedException) {
            logger.severe("Failed shutting down. Error: " + e.message)
        }
    }

    /**
     * Gets the manager responsible for handling GUI interpreters.
     *
     * @return The InterpreterManager instance.
     */
    fun getInterpreterManager(): InterpreterManager {
        return interpreterManager!!
    }

    /**
     * Gets the manager responsible for managing GUI views.
     *
     * @return The DefaultGuiViewManager instance.
     */
    fun getGuiViewManager(): DefaultGuiViewManager {
        return guiViewManager!!
    }

    /**
     * Creates the manager instances for GUI views and interpreters.
     */
    private fun createManagers() {
        guiViewManager = DefaultGuiViewManager()
        interpreterManager = InterpreterManager()
        guiItemManager = GuiItemManager(config)
    }

    /**
     * Registers the supported GUI components and their corresponding builders to the GuiEngineApi.
     * This allows the creation of instances with these components from their respective builders.
     */
    private fun registerComponents() {
        GuiEngineApi.registerSharedFactory(
            SimpleItemComponent.TYPE,
            SimpleItemComponent::class.java,
            SimpleItemComponentBuilder::class.java
        )
        GuiEngineApi.registerSharedFactory(
            EmbeddedGuiComponent.TYPE,
            EmbeddedGuiComponent::class.java,
            EmbeddedGuiComponentBuilder::class.java
        )
        GuiEngineApi.registerSharedFactory(
            ToggleItemComponent.TYPE,
            ToggleItemComponent::class.java,
            ToggleItemComponentBuilder::class.java
        )
        GuiEngineApi.registerSharedFactory(
            PagedComponent.TYPE,
            PagedComponent::class.java,
            PagedComponentBuilder::class.java
        )
    }

    /**
     * Checks for updates
     */
    private fun checkForUpdate() {
        if (!config.getBoolean("update-checker")) return
        val pattern = Pattern.compile("[^0-9]")
        val checker = UpdateChecker(this, 109983)
        checker.getVersion { version: String? ->
            val latest = version?.let { pattern.matcher(it).replaceAll("") }
            val current = pattern.matcher(description.version).replaceAll("")
            if (latest == current) return@getVersion
            LATEST_VERSION = false
            Bukkit.getOnlinePlayers().forEach { player: Player? -> PlayerJoinListener.send(player!!) }
            Bukkit.getConsoleSender().sendMessage(
                String.format(
                    "§bA newer version of §eGuiEngine§b is available on §espigotmc.org§b. §e%s §b-> §e%s",
                    description.version,
                    version
                )
            )
        }
    }

    /**
     * Registers the default functions for GUI interactions to the FunctionProcessor.
     * These functions include AddComponentsFunction, EditComponentFunction, RemoveComponentFunction, and ActionFunction.
     * Additionally, it registers default compute functions for evaluating GUI component properties and permissions.
     */
    private fun registerFunctions() {
        FunctionProcessor.registerFunction(AddComponentsFunction.TYPE, AddComponentsFunction::class.java)
        FunctionProcessor.registerFunction(EditComponentFunction.TYPE, EditComponentFunction::class.java)
        FunctionProcessor.registerFunction(RemoveComponentFunction.TYPE, RemoveComponentFunction::class.java)
        FunctionProcessor.registerFunction(ActionFunction.TYPE, ActionFunction::class.java)
        FunctionProcessor.registerFunction(DelayFunction.TYPE, DelayFunction::class.java)
        FunctionProcessor.registerComputeFunction(GuiComponentPropertyFunction())
        FunctionProcessor.registerComputeFunction(HasPermissionFunction())
        FunctionProcessor.registerComputeFunction(HasNotPermissionFunction())
    }

    /**
     * Registers custom commands provided by the plugin using the GuiCommands class.
     */
    private fun registerCommands() {
        GuiCommands()
    }

    /**
     * Creates and adds a default API instance named "default" for handling GUI data.
     * The API is initialized with a directory for storing GUI configurations.
     * If an exception occurs while reloading the API, it is ignored.
     */
    private fun addDefaultApi() {
        guiApi = GuiEngineApi("default", File(dataFolder, "guis"))
        try {
            guiApi!!.reload()
        } catch (ignored: GuiIORuntimeException) {
            // Ignored if an exception occurs while reloading the API.
        }
    }

    /**
     * Registers the default action for GUI interactions, specifically the OpenGuiAction.
     */
    private fun registerActions() {
        ActionCore.bindActions(this)
        ActionCore.register(OpenGuiAction())
    }

    /**
     * Registers the default interpreter, DefaultInterpreter, to the InterpreterManager.
     */
    private fun registerInterpreters() {
        interpreterManager?.registerInterpreter(SizeableInterpreter())
        interpreterManager?.registerInterpreter(ChestInterpreter())
    }

    /**
     * Registers the GUI view manager to Bukkit's event system for handling GUI events.
     */
    private fun registerListeners() {
        server.pluginManager.registerEvents(guiViewManager!!, this)
        server.pluginManager.registerEvents(ItemClickListener(), this)
        server.pluginManager.registerEvents(PlayerJoinListener(), this)
    }

    companion object {
        /**
         * The current version of this plugin
         */
        var LATEST_VERSION = true

        /**
         * The singleton instance of the GuiEngineApiPlugin.
         */
        private var instance: GuiEngineApiPlugin? = null

        val plugin: GuiEngineApiPlugin
            /**
             * Returns the singleton instance of the GuiEngineApiPlugin.
             *
             * @return The GuiEngineApiPlugin instance.
             */
            get() = instance!!
    }
}