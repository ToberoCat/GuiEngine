package io.github.toberocat.guiengine.components.provided.item;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.guiengine.utils.ParserContext;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

public class SimpleItemComponentBuilder<B extends SimpleItemComponentBuilder<B>> extends AbstractGuiComponentBuilder<B> {
    protected @NotNull String name = "";
    protected @Nullable Material material;
    protected @NotNull String[] lore = new String[0];
    protected @Nullable String textureId;
    protected @Nullable UUID owner;


    public @NotNull B setTextureId(@Nullable String textureId) {
        this.textureId = textureId;
        return self();
    }

    public @NotNull B setOwner(@Nullable UUID owner) {
        this.owner = owner;
        return self();
    }

    public @NotNull B setName(@NotNull String name) {
        this.name = name;
        return self();
    }

    public @NotNull B setMaterial(@NotNull Material material) {
        this.material = material;
        return self();
    }

    public @NotNull B setLore(@NotNull String[] lore) {
        this.lore = lore;
        return self();
    }

    @Override
    public @NotNull SimpleItemComponent createComponent() {
        ItemStack stack;
        if (material == null) {
            stack = textureId != null
                    ? ItemUtils.createHead(textureId, name, 1, lore)
                    : owner != null ?
                    ItemUtils.createSkull(Bukkit.getOfflinePlayer(owner), 1, name, lore)
                    : ItemUtils.createItem(Material.PLAYER_HEAD, name, 1, lore);
        } else {
            stack = ItemUtils.createItem(material, name, 1, lore);
        }

        return new SimpleItemComponent(x, y, priority, id, clickFunctions, dragFunctions, closeFunctions, stack, hidden);
    }

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);

        setName(node.getOptionalString("name").orElse(" "));
        setLore(node.getOptionalStringArray("lore").orElse(new String[0]));

        String texture = node.getOptionalString("head-texture").map(String::trim).orElse(null);
        UUID headOwner = node.getOptionalUUID("head-owner").orElse(null);
        if (texture != null || headOwner != null) {
            setOwner(headOwner);
            setTextureId(texture);
        } else {
            setMaterial(node.getOptionalMaterial("material").orElseThrow(() ->
                    new MissingRequiredParamException(this, "material")));
        }
    }
}