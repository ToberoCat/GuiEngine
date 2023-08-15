package io.github.toberocat.guiengine.arguments;

import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import io.github.toberocat.toberocore.command.arguments.Argument;
import io.github.toberocat.toberocore.command.exceptions.CommandException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class GuiEngineApiArgument implements Argument<GuiEngineApi> {
    private final GuiEngineApi defaultApi;

    public GuiEngineApiArgument() {
        this(GuiEngineApiPlugin.getPlugin().getGuiApi());
    }

    public GuiEngineApiArgument(GuiEngineApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    @Override
    public GuiEngineApi parse(@NotNull Player player, @NotNull String s) throws CommandException {
        GuiEngineApi api = GuiEngineApi.APIS.get(s);
        if (api == null)
            throw new CommandException("§cNo API found with ID" + s, new HashMap<>());
        return api;
    }

    @Override
    public GuiEngineApi defaultValue(@NotNull Player player) {
        return defaultApi;
    }

    @Override
    public @Nullable List<String> tab(@NotNull Player player) throws CommandException {
        return GuiEngineApi.APIS.keySet().stream().toList();
    }

    @Override
    public @NotNull String descriptionKey() {
        return "base.gui-engine.args.api";
    }

    @Override
    public @NotNull String usage() {
        return "<api>";
    }
}
