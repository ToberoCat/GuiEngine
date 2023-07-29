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

/**
 * A builder class for creating SimpleItemComponent instances.
 * This builder provides methods for setting various properties of the SimpleItemComponent.
 *
 * @param <B> The type of the builder, used for method chaining.
 */
public class SimpleItemComponentBuilder<B extends SimpleItemComponentBuilder<B>> extends AbstractGuiComponentBuilder<B> {
    protected @NotNull String name = "";
    protected @Nullable Material material;
    protected @NotNull String[] lore = new String[0];
    protected @Nullable String textureId;
    protected @Nullable UUID owner;


    /**
     * Set the texture ID for the item (only applicable to PLAYER_HEAD or SKULL items).
     *
     * @param textureId The texture ID for the item.
     * @return The builder instance (for method chaining).
     */
    public @NotNull B setTextureId(@Nullable String textureId) {
        this.textureId = textureId;
        return self();
    }

    /**
     * Set the owner UUID for the item (only applicable to PLAYER_HEAD or SKULL items).
     *
     * @param owner The owner UUID for the item.
     * @return The builder instance (for method chaining).
     */
    public @NotNull B setOwner(@Nullable UUID owner) {
        this.owner = owner;
        return self();
    }

    /**
     * Set the name of the item.
     *
     * @param name The name of the item.
     * @return The builder instance (for method chaining).
     */
    public @NotNull B setName(@NotNull String name) {
        this.name = name;
        return self();
    }

    /**
     * Set the material of the item (e.g., DIAMOND, GOLD_INGOT, etc.).
     *
     * @param material The material of the item.
     * @return The builder instance (for method chaining).
     */
    public @NotNull B setMaterial(@NotNull Material material) {
        this.material = material;
        return self();
    }

    /**
     * Set the lore of the item.
     *
     * @param lore The lore of the item as an array of strings.
     * @return The builder instance (for method chaining).
     */
    public @NotNull B setLore(@NotNull String[] lore) {
        this.lore = lore;
        return self();
    }

    /**
     * Creates the item stack represented by the builder's settings
     *
     * @return The item-stack
     */
    protected @NotNull ItemStack getItemStack() {
        if (null != material) return ItemUtils.createItem(material, name, 1, lore);
        return null != textureId ? ItemUtils.createHead(textureId, name, 1, lore) : null != owner ? ItemUtils.createSkull(Bukkit.getOfflinePlayer(owner), 1, name, lore) : ItemUtils.createItem(Material.PLAYER_HEAD, name, 1, lore);
    }

    @Override
    public @NotNull SimpleItemComponent createComponent() {
        return new SimpleItemComponent(x, y, priority, id, clickFunctions, dragFunctions, closeFunctions, getItemStack(), hidden);
    }

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);

        setName(node.getOptionalString("name").orElse(" "));
        setLore(node.getOptionalStringArray("lore").orElse(new String[0]));

        String texture = node.getOptionalString("head-texture").map(String::trim).orElse(null);
        UUID headOwner = node.getOptionalUUID("head-owner").orElse(null);
        if (null != texture || null != headOwner) {
            setOwner(headOwner);
            setTextureId(texture);
        } else {
            setMaterial(node.getOptionalMaterial("material").orElseThrow(() -> new MissingRequiredParamException(this, "material")));
        }
    }
}