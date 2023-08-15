package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.toberocore.command.SubCommand;
import io.github.toberocat.toberocore.command.arguments.Argument;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import io.github.toberocat.toberocore.command.options.Options;
import io.github.toberocat.toberocore.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GiveCommand extends SubCommand {
    public static NamespacedKey API_NAME_KEY;
    public static NamespacedKey GUI_ID_KEY;

    private final @NotNull GuiEngineApiPlugin plugin;

    public GiveCommand(@NotNull GuiEngineApiPlugin plugin) {
        super("give");
        this.plugin = plugin;
        API_NAME_KEY = new NamespacedKey(plugin, "api_key");
        GUI_ID_KEY = new NamespacedKey(plugin, "gui_id");
    }

    @Override
    protected boolean handleCommand(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (3 > args.length)
            throw new CommandException("You need to give this command four arguments: §6/guiengine give <item> <api> <gui> <player>§c. The last one is optional", new HashMap<>());
        ItemStack stack = plugin.getGuiItemManager().getItem(args[0]);
        if (null == stack) throw new CommandException("The item §6'" + args[0] + "'§c can't be found", new HashMap<>());

        GuiEngineApi api = GuiEngineApi.APIS.get(args[1]);
        if (null == api) throw new CommandException("§cNo API found with ID " + args[1], new HashMap<>());

        String guiId = args[2];
        if (!api.getAvailableGuis().contains(guiId))
            throw new CommandException("This gui doesn't exist in the specified api", new HashMap<>());

        Player target = null;
        if (sender instanceof Player player) target = player;
        if (4 == args.length) target = Bukkit.getPlayer(args[3]);
        if (null == target) throw new CommandException("Player not found", new HashMap<>());


        stack = new ItemBuilder(stack.clone()).persistent(API_NAME_KEY, PersistentDataType.STRING, api.getId()).persistent(GUI_ID_KEY, PersistentDataType.STRING, guiId).create(plugin);

        target.getInventory().addItem(stack);
        if (target != sender) sender.sendMessage("§aYou gave §e" + target.getName() + "§a the item successfully");

        return true;
    }

    @Override
    protected @NotNull Options options() {
        return new Options();
    }

    @Override
    protected @NotNull Argument<?>[] arguments() {
        return new Argument[0];
    }

    @Override
    public @Nullable List<String> routeTab(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!sender.hasPermission(this.getPermission())) {
            return null;
        }

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        if (1 >= newArgs.length) return GuiEngineApi.APIS.keySet().stream().toList();
        GuiEngineApi api = GuiEngineApi.APIS.get(newArgs[0]);
        if (null == api) {
            sender.sendMessage("§cNo API found with ID " + newArgs[0]);
            return Collections.emptyList();
        }

        if (3 == args.length) return api.getAvailableGuis().stream().toList();
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
    }
}
