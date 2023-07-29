package io.github.toberocat.guiengine.commands;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.toberocore.command.SubCommand;
import io.github.toberocat.toberocore.command.exceptions.CommandExceptions;
import io.github.toberocat.toberocore.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GiveCommand extends SubCommand {
    public static @NotNull NamespacedKey API_NAME_KEY;
    public static @NotNull NamespacedKey GUI_ID_KEY;

    private final @NotNull GuiEngineApiPlugin plugin;

    public GiveCommand(@NotNull GuiEngineApiPlugin plugin) {
        super("give");
        this.plugin = plugin;
        API_NAME_KEY = new NamespacedKey(plugin, "api_key");
        GUI_ID_KEY = new NamespacedKey(plugin, "gui_id");
    }

    @Override
    protected boolean run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) throws CommandExceptions {
        if (3 > args.length)
            throw new CommandExceptions("You need to give this command four arguments: §6/guiengine give <item> <api> <gui> <player>§c. The last one is optional");
        ItemStack stack = plugin.getGuiItemManager().getItem(args[0]);
        if (null == stack) throw new CommandExceptions("The item §6'" + args[0] + "'§c can't be found");

        GuiEngineApi api = GuiEngineApi.APIS.get(args[1]);
        if (null == api) throw new CommandExceptions("§cNo API found with ID " + args[1]);

        String guiId = args[2];
        if (!api.getAvailableGuis().contains(guiId))
            throw new CommandExceptions("This gui doesn't exist in the specified api");

        Player target = null;
        if (sender instanceof Player player) target = player;
        if (4 == args.length) target = Bukkit.getPlayer(args[3]);
        if (null == target) throw new CommandExceptions("Player not found");


        stack = new ItemBuilder(stack.clone())
                .persistent(API_NAME_KEY, PersistentDataType.STRING, api.getId())
                .persistent(GUI_ID_KEY, PersistentDataType.STRING, guiId)
                .create();

        target.getInventory().addItem(stack);
        if (target != sender)
            sender.sendMessage("§aYou gave §e" + target.getName() + "§a the item successfully");

        return true;
    }

    @Override
    protected @Nullable List<String> runTab(@NotNull CommandSender player, @NotNull String @NotNull [] args) {
        if (1 >= args.length) return new ArrayList<>(plugin.getGuiItemManager().getKnownItems().keySet());
        if (2 == args.length) return GuiEngineApi.APIS.keySet().stream().toList();

        GuiEngineApi api = GuiEngineApi.APIS.get(args[1]);
        if (null == api) {
            player.sendMessage("§cNo API found with ID " + args[1]);
            return Collections.emptyList();
        }

        if (3 == args.length) return api.getAvailableGuis().stream().toList();
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
    }
}
