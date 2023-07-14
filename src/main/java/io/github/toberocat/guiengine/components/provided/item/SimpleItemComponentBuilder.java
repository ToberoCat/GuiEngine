package io.github.toberocat.guiengine.components.provided.item;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.guiengine.utils.ParserContext;
import io.github.toberocat.toberocore.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);
        setName(node.getOptionalString("name").orElse(" "));
        setLore(node.getOptionalStringArray("lore").orElse(new String[0]));
        setMaterial(node.getOptionalMaterial("material").orElseThrow(() ->
                new MissingRequiredParamException(this, "material")));
    }
}