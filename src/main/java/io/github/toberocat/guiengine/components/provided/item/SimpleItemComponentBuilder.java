package io.github.toberocat.guiengine.components.provided.item;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.github.toberocat.guiengine.utils.JsonUtils.*;

public class SimpleItemComponentBuilder<B extends SimpleItemComponentBuilder<B>> extends AbstractGuiComponentBuilder<B> {
    protected @NotNull String name = "";
    protected @NotNull Material material = Material.AIR;
    protected @NotNull String[] lore = new String[0];

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
        ItemStack stack = ItemUtils.createItem(material, name, 1, lore);
        return new SimpleItemComponent(x, y, priority, id, clickFunctions, dragFunctions, closeFunctions, stack, hidden);
    }

    @Override
    public void deserialize(@NotNull JsonNode node) throws IOException {
        super.deserialize(node);
        setName(getOptionalString(node, "name").orElse(" "));
        setMaterial(getOptionalMaterial(node, "material").orElseThrow(() -> new MissingRequiredParamException(this, "material")));
        setLore(getOptionalStringArray(node, "lore").orElse(new String[0]));
    }
}