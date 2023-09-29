package io.github.toberocat.guiengine

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.toberocat.guiengine.components.GuiComponent
import io.github.toberocat.guiengine.components.GuiComponentBuilder
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.exception.GuiIORuntimeException
import io.github.toberocat.guiengine.exception.GuiNotFoundRuntimeException
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.exception.InvalidGuiFileException
import io.github.toberocat.guiengine.interpreter.InterpreterManager
import io.github.toberocat.guiengine.utils.FileUtils
import io.github.toberocat.guiengine.utils.VirtualInventory
import io.github.toberocat.guiengine.utils.VirtualPlayer
import io.github.toberocat.guiengine.utils.VirtualView
import io.github.toberocat.guiengine.utils.logger.GuiLogger
import io.github.toberocat.guiengine.view.DefaultGuiViewManager
import io.github.toberocat.guiengine.xml.GuiComponentDeserializer
import io.github.toberocat.guiengine.xml.GuiComponentSerializer
import io.github.toberocat.guiengine.xml.XmlGui
import org.apache.commons.text.StringSubstitutor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.nio.file.Files
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.math.sqrt

/**
 * The `GuiEngineApi` class provides an API to manage and interact with graphical user interfaces (GUIs).
 * It allows you to register and manage various GUI components, load XML-based GUI definitions,
 * and display GUIs to players in the game.
 *
 *
 * This class is licensed under the GNU General Public License.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 05/02/2023
 */
class GuiEngineApi(
    id: String, private val guiFolder: File, private val guiFilter: FileFilter = DEFAULT_GUI_FILTER
) {
    /**
     * Returns the `ObjectMapper` used for XML serialization/deserialization of GUI components.
     *
     * @return The `ObjectMapper` instance for XML handling.
     */
    val xmlMapper: XmlMapper = XmlMapper()
    private val componentIdMap: MutableMap<String, Class<out GuiComponent>> = HashMap(SHARED_COMPONENT_ID_MAPS)
    private val plugin: GuiEngineApiPlugin = GuiEngineApiPlugin.plugin
    private var availableGuis: MutableSet<String> = mutableSetOf()
    val id: String

    /**
     * Constructs a new `GuiEngineApi` instance for the specified plugin.
     * The ID will be automatically set to the plugin's name, and the gui folder will be the data-folder/guis
     *
     *
     * This has the advantage that GuiEngine automatically copies the guis folder from the resources
     *
     * @param plugin The plugin this API owns to
     */
    constructor(plugin: JavaPlugin) : this(plugin.name, FileUtils.copyAll(plugin, "guis"))

    init {
        availableGuis = HashSet()
        xmlMapper.registerModules(SHARED_MODULES)
        xmlMapper.registerModule(kotlinModule())
        xmlMapper.factory.xmlTextElementName = "$"

        this.id = GUI_ID_REGEX.matcher(id).replaceAll("")
        when {
            !guiFolder.exists() && !guiFolder.mkdirs() -> Bukkit.getLogger()
                .severe("Couldn't create gui folder " + guiFolder.absolutePath)

            else -> APIS[id] = this
        }
    }

    /**
     * Registers a GUI component factory with the given ID, class, and builder class.
     *
     * @param id           The ID of the GUI component factory.
     * @param clazz        The class representing the GUI component.
     * @param builderClazz The class representing the GUI component builder.
     * @param <T> The type of the GUI component.
     * @param <B> The type of the GUI component builder.
     */
    fun <T : GuiComponent, B : GuiComponentBuilder?> registerFactory(
        id: String, clazz: Class<T>, builderClazz: Class<B>
    ) {
        componentIdMap[id] = clazz
        val module = SimpleModule()
        module.addSerializer(clazz, GuiComponentSerializer(clazz))
        module.addDeserializer(clazz, GuiComponentDeserializer(builderClazz))
        xmlMapper.registerModules(module)
    }

    /**
     * Reloads the available GUIs from the GUI folder.
     *
     * @throws GuiIORuntimeException If there is an I/O error while loading or validating GUIs.
     */
    @Throws(GuiIORuntimeException::class)
    fun reload(logger: GuiLogger) {
        availableGuis =
            listGuis(guiFolder).stream().map { file: File -> guiIdFromFile(file) }.collect(Collectors.toSet())

        val totals = LongArray(availableGuis.size)
        val guisCopy = ArrayList(availableGuis)

        for (i in guisCopy.indices) totals[i] = validateGui(logger, guisCopy[i])

        val avg = Arrays.stream(totals).average().orElse(Double.NaN)
        val sumSquaredDifferences =
            Arrays.stream(totals).mapToDouble { total: Long -> (total - avg) * (total - avg) }.sum()

        val populationSize = totals.size
        val variance = sumSquaredDifferences / populationSize
        val std = sqrt(variance)
        Bukkit.getConsoleSender().sendMessage(
            String.format(
                "[GuiEngine] §bIt takes §e%.3fms ± %.3fms§b on average to render a gui from §e%s", avg, std, id
            )
        )
    }

    /**
     * Opens a GUI with the specified ID for the given player, using the default placeholders.
     *
     * @param player The player to whom the GUI should be displayed.
     * @param guiId  The ID of the GUI to open.
     * @return The `GuiContext` representing the opened GUI.
     * @throws GuiNotFoundRuntimeException If the specified GUI ID does not correspond to an existing GUI.
     * @throws GuiIORuntimeException       If there is an I/O error while loading or rendering the GUI.
     */
    @Throws(GuiNotFoundRuntimeException::class, GuiIORuntimeException::class)
    fun openGui(
        player: Player, guiId: String, placeholders: Map<String, String> = getGuiPlaceholders(player)
    ): GuiContext {
        val xmlGui = loadXmlGui(placeholders, guiId)

        val interpreter = plugin.getInterpreterManager().getInterpreter(xmlGui.interpreter)
            ?: throw GuiIORuntimeException("No interpreter found for " + xmlGui.interpreter)

        val context = interpreter.loadContent(this, player, xmlGui)
        interpreter.renderEngine.showGui(context, player, placeholders)
        return context
    }

    /**
     * Loads and returns the XML-based GUI definition with the specified ID, substituting the placeholders.
     *
     * @param placeholders The map of placeholders to be substituted in the GUI content.
     * @param guiId        The ID of the GUI to load.
     * @return The loaded `XmlGui` instance representing the GUI.
     * @throws GuiNotFoundRuntimeException If the specified GUI ID does not correspond to an existing GUI.
     * @throws GuiIORuntimeException       If there is an I/O error while loading the GUI.
     */
    @Throws(GuiNotFoundRuntimeException::class, GuiIORuntimeException::class)
    fun loadXmlGui(placeholders: Map<String, String>, guiId: String): XmlGui {
        val gui = File(guiFolder, "$guiId.gui")
        if (!gui.exists()) throw GuiNotFoundRuntimeException(guiId)
        return try {
            var content = Files.readString(gui.toPath())
            content = StringSubstitutor.replace(content, placeholders, "%", "%")
            content = content.replace("<gui", "<gui api='$id'")
            val xml = xmlMapper.readValue(content, XmlGui::class.java)
            xml.guiId = guiId
            xml
        } catch (e: JsonParseException) {
            throw InvalidGuiFileException("Couldn't parse $guiId.gui. Caused by: ${e.message}. Check your guis syntax and validate that it isn't empty")
        } catch (e: IOException) {
            throw GuiIORuntimeException(e)
        }
    }

    /**
     * Converts a file into it's guiId.
     * This takes the extension away and returns the part before
     *
     * @param file The input file
     * @return The guiId of this file
     */
    private fun guiIdFromFile(file: File): String {
        val gui = file.toPath()
        val folder = guiFolder.toPath()
        return folder.relativize(gui).toString().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    /**
     * Returns the set of available GUI IDs within the GUI folder.
     *
     * @return The set of available GUI IDs.
     */
    fun getAvailableGuis(): Set<String> = availableGuis

    val interpreterManager: InterpreterManager
        get() = plugin.getInterpreterManager()
    val guiViewManager: DefaultGuiViewManager
        get() = plugin.getGuiViewManager()

    fun getComponentIdMap(): Map<String, Class<out GuiComponent>> = componentIdMap
    private fun listGuis(folder: File): List<File> {
        val files = folder.listFiles()
        val guis: MutableList<File> = LinkedList()
        if (null == files) return guis

        for (file in files) {
            if (file.isDirectory()) {
                guis.addAll(listGuis(file))
                continue
            }
            if (!guiFilter.accept(file)) continue
            guis.add(file)
        }
        return guis
    }

    @Throws(GuiIORuntimeException::class)
    private fun validateGui(logger: GuiLogger, gui: String): Long {
        var total: Long = 0
        var delta: Long
        try {
            logger.debug("Validating $gui.gui")
            val virtualPlayer = VirtualPlayer()
            var now = System.currentTimeMillis()
            val xmlGui = loadXmlGui(getGuiPlaceholders(virtualPlayer), gui)
            delta = System.currentTimeMillis() - now
            logger.debug("Took ${delta}ms parsing $gui")
            total += delta
            now = System.currentTimeMillis()
            val interpreter = plugin.getInterpreterManager().getInterpreter(xmlGui.interpreter)
                ?: throw GuiIORuntimeException("No interpreter found for " + xmlGui.interpreter)
            val context = interpreter.loadContent(this, virtualPlayer, xmlGui)
            delta = System.currentTimeMillis() - now
            logger.debug("Took ${delta}ms creating a context for $gui")
            total += delta
            now = System.currentTimeMillis()
            val renderTask = CompletableFuture.supplyAsync<Exception?> {
                val virtualInventory = VirtualInventory(interpreter.renderEngine.height(context)) {}
                val virtualBuffer = interpreter.renderEngine.createBuffer(context)
                context.setViewer(virtualPlayer)
                context.setInventory(virtualInventory)

                try {
                    context.componentsDescending().forEach { it?.onViewInit(HashMap()) }
                    interpreter.renderEngine.renderGui(virtualBuffer, context, virtualPlayer)
                } catch (e: Exception) {
                    return@supplyAsync e
                }
                null
            }

            try {
                val exception = renderTask[1, TimeUnit.SECONDS]
                if (null != exception) throw exception
                delta = System.currentTimeMillis() - now
                logger.debug("Took ${delta}ms rendering to a virtual player $gui")
            } catch (e: TimeoutException) {
                renderTask.cancel(true)
                logger.error("Stopped rendering gui to virtual " + "player. The render process is very likely to render an infinite long period")
                throw e
            } catch (e: StackOverflowError) {
                renderTask.cancel(true)
                logger.error("Stopped rendering gui to virtual " + "player. The render process is very likely to render an infinite long period")
                throw e
            }
            total += delta
            logger.debug("§aTook in total ${delta}ms§a to get $gui displayed to the virtual player")
            context.closedComponent(InventoryCloseEvent(VirtualView(virtualPlayer)))
        } catch (e: InvalidGuiComponentException) {
            availableGuis.remove(gui)
            logger.error(String.format("%s.gui has a invalid component. %s", gui, e.message))
        } catch (e: InvalidGuiFileException) {
            availableGuis.remove(gui)
            logger.error(e.message)
        } catch (e: Throwable) {
            availableGuis.remove(gui)
            e.printStackTrace()
            logger.error("The gui couldn't get rendered to an virtual player. Please take a look at it")
            throw GuiIORuntimeException(e)
        }
        return total
    }

    private fun getGuiPlaceholders(viewer: Player): Map<String, String> {
        val placeholders: MutableMap<String, String> = HashMap()
        placeholders["viewer"] = viewer.uniqueId.toString()
        return placeholders
    }

    companion object {
        val APIS: MutableMap<String, GuiEngineApi> = HashMap()

        val LOADED_CONTEXTS: MutableMap<UUID, GuiContext> = HashMap()
        val DEFAULT_GUI_FILTER = FileFilter { file: File -> file.getName().endsWith(".gui") }

        val SHARED_MODULES: MutableList<SimpleModule> = LinkedList()
        val GUI_ID_REGEX: Pattern = Pattern.compile("[^a-zA-Z_-]", Pattern.MULTILINE)
        val SHARED_COMPONENT_ID_MAPS: MutableMap<String, Class<out GuiComponent>> = HashMap()

        /**
         * Registers a shared GUI component factory with the given ID, class, and builder class.
         * Shared factories can be used across different instances of `GuiEngineApi`.
         *
         * @param id           The ID of the shared GUI component factory.
         * @param clazz        The class representing the GUI component.
         * @param builderClazz The class representing the GUI component builder.
         * @param <T> The type of the GUI component.
         * @param <B> The type of the GUI component builder.
        </B></T> */
        fun <T : GuiComponent, B : GuiComponentBuilder?> registerSharedFactory(
            id: String, clazz: Class<T>, builderClazz: Class<B>
        ) {
            SHARED_COMPONENT_ID_MAPS[id] = clazz
            val module = SimpleModule()
            module.addSerializer(clazz, GuiComponentSerializer(clazz))
            module.addDeserializer(clazz, GuiComponentDeserializer(builderClazz))
            SHARED_MODULES.add(module)
        }
    }
}
