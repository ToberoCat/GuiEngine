package io.github.toberocat.guiengine.components.provided.head;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder;
import io.github.toberocat.guiengine.utils.ParserContext;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

import static io.github.toberocat.guiengine.utils.JsonUtils.getOptionalString;
import static io.github.toberocat.guiengine.utils.JsonUtils.getOptionalUUID;

public class HeadItemComponentBuilder<B extends HeadItemComponentBuilder<B>> extends SimpleItemComponentBuilder<B> {
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

    @Override
    public @NotNull HeadItemComponent createComponent() {
        ItemStack item = textureId != null
                ? ItemUtils.createHead(textureId, name, 1, lore)
                : owner != null ?
                ItemUtils.createSkull(Bukkit.getOfflinePlayer(owner), 1, name, lore)
                : ItemUtils.createItem(Material.PLAYER_HEAD, name, 1, lore);

        return new HeadItemComponent(x, y, priority, id, clickFunctions, dragFunctions, closeFunctions, item, hidden);
    }

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);
        setTextureId(node.getOptionalString("head-texture").orElse(null));
        setOwner(node.getOptionalUUID("head-owner").orElse(null));
    }
}