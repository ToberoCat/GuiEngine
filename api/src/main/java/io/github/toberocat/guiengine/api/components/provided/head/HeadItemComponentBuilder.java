package io.github.toberocat.guiengine.api.components.provided.head;

import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.toberocore.util.ItemBuilder;
import io.github.toberocat.toberocore.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeadItemComponentBuilder {
    private @NotNull RenderPriority priority = RenderPriority.NORMAL;
    private @NotNull String id = UUID.randomUUID().toString();
    private @NotNull String name = "";
    private @Nullable String textureId;
    private @Nullable UUID owner;
    private @NotNull String[] lore = new String[0];
    private @NotNull List<GuiFunction> clickFunctions = new ArrayList<>();
    private int x = 0;
    private int y = 0;
    private boolean hidden = false;

    public @NotNull HeadItemComponentBuilder setPriority(@NotNull RenderPriority priority) {
        this.priority = priority;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setTextureId(@Nullable String textureId) {
        this.textureId = textureId;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setOwner(@Nullable UUID owner) {
        this.owner = owner;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setLore(@NotNull String[] lore) {
        this.lore = lore;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setClickFunctions(@NotNull List<GuiFunction> clickFunctions) {
        this.clickFunctions = clickFunctions;
        return this;
    }

    public @NotNull HeadItemComponentBuilder addClickFunction(@NotNull GuiFunction clickFunction) {
        clickFunctions.add(clickFunction);
        return this;
    }

    public @NotNull HeadItemComponentBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public @NotNull HeadItemComponentBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public HeadItemComponent createHeadItemComponent() {
        ItemStack item = textureId != null
                ? new ItemStack(Material.AIR) // ItemUtils.createHead(textureId, name, 1, lore)
                : owner != null ?
                ItemUtils.createSkull(Bukkit.getOfflinePlayer(owner), 1, name, lore)
                : ItemUtils.createItem(Material.PLAYER_HEAD, name, 1, lore);

        HeadItemComponent component = new HeadItemComponent(priority, id, item, clickFunctions, x, y);
        component.setHidden(hidden);
        return component;
    }
}