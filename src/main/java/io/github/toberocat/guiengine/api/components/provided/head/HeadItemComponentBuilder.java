package io.github.toberocat.guiengine.api.components.provided.head;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.components.provided.item.SimpleItemComponentBuilder;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

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

    public static class Factory<B extends HeadItemComponentBuilder<B>> extends SimpleItemComponentBuilder.Factory<B> {
        @Override
        public @NotNull B createBuilder() {
            return (B) new HeadItemComponentBuilder<B>();
        }

        @Override
        public void deserialize(@NotNull JsonNode node, @NotNull B builder) throws IOException {
            super.deserialize(node, builder);
            builder.setTextureId(getOptionalString(node, "head-texture").orElse(null))
                    .setOwner(getOptionalUUID(node, "head-owner").orElse(null));
        }
    }
}