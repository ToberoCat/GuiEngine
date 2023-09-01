package io.github.toberocat.guiengine.commands

import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.GuiEngineApiPlugin
import io.github.toberocat.toberocore.command.SubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import io.github.toberocat.toberocore.util.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
class GiveCommand(private val plugin: GuiEngineApiPlugin) : SubCommand("give") {
    init {
        API_NAME_KEY = NamespacedKey(plugin, "api_key")
        GUI_ID_KEY = NamespacedKey(plugin, "gui_id")
    }

    @Throws(CommandException::class)
    override fun handleCommand(sender: CommandSender, args: Array<String>): Boolean {
        if (3 > args.size) throw CommandException(
            "You need to give this command four arguments: §6/guiengine give <item> <api> <gui> <player>§c. The last one is optional",
            HashMap()
        )
        var stack = plugin.guiItemManager?.get(args[0])
            ?: throw CommandException(
                "The item §6'" + args[0] + "'§c can't be found",
                HashMap()
            )
        val api = GuiEngineApi.APIS[args[1]]
            ?: throw CommandException(
                "§cNo API found with ID " + args[1],
                HashMap()
            )
        val guiId = args[2]
        if (!api.getAvailableGuis()
                .contains(guiId)
        ) throw CommandException("This gui doesn't exist in the specified api", HashMap())
        var target: Player? = null
        if (sender is Player) target = sender
        if (4 == args.size) target = Bukkit.getPlayer(args[3])
        if (null == target) throw CommandException("Player not found", HashMap())
        stack = ItemBuilder(stack.clone()).persistent(API_NAME_KEY, PersistentDataType.STRING, api.id).persistent(
            GUI_ID_KEY, PersistentDataType.STRING, guiId
        ).create(
            plugin
        )
        target.inventory.addItem(stack)
        if (target !== sender) sender.sendMessage("§aYou gave §e" + target.name + "§a the item successfully")
        return true
    }

    override fun options(): Options {
        return Options()
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    @Throws(CommandException::class)
    override fun routeTab(sender: CommandSender, args: Array<String>): List<String>? {
        if (!sender.hasPermission(permission)) {
            return null
        }
        val newArgs = arrayOfNulls<String>(args.size - 1)
        System.arraycopy(args, 1, newArgs, 0, newArgs.size)
        if (1 >= newArgs.size) return GuiEngineApi.APIS.keys.stream().toList()
        val api = GuiEngineApi.APIS[newArgs[0]]
        if (null == api) {
            sender.sendMessage("§cNo API found with ID " + newArgs[0])
            return emptyList()
        }
        return if (3 == args.size) api.getAvailableGuis().stream().toList() else Bukkit.getOnlinePlayers().stream()
            .map { obj: HumanEntity -> obj.name }
            .toList()
    }

    companion object {
        lateinit var API_NAME_KEY: NamespacedKey
        lateinit var GUI_ID_KEY: NamespacedKey
    }
}
