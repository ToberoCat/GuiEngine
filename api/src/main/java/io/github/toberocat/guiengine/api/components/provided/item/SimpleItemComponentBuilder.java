package io.github.toberocat.guiengine.api.components.provided.item;

import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.toberocore.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleItemComponentBuilder {
    private @NotNull RenderPriority priority = RenderPriority.NORMAL;
    private @NotNull String id = UUID.randomUUID().toString();
    private @NotNull String name = "";
    private @NotNull Material material = Material.AIR;
    private @NotNull String[] lore = new String[0];
    private @NotNull List<GuiFunction> clickFunctions = new ArrayList<>();
    private int x = 0;
    private int y = 0;
    private boolean hidden = false;

    public @NotNull SimpleItemComponentBuilder setPriority(@NotNull RenderPriority priority) {
        this.priority = priority;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setLore(@NotNull String[] lore) {
        this.lore = lore;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setClickFunctions(@NotNull List<GuiFunction> clickFunctions) {
        this.clickFunctions = clickFunctions;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public @NotNull SimpleItemComponentBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public @NotNull SimpleItemComponent createSimpleItemComponent() {
        ItemStack stack = ItemUtils.createItem(material, name, 1, lore);
        SimpleItemComponent component = new SimpleItemComponent(priority, id, stack, clickFunctions, x, y);
        component.setHidden(hidden);
        return component;
    }
}