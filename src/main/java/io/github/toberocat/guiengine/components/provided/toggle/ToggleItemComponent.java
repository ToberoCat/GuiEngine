package io.github.toberocat.guiengine.components.provided.toggle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.Selectable;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.JsonUtils;
import io.github.toberocat.guiengine.utils.ParserContext;
import io.github.toberocat.guiengine.xml.XmlComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ToggleItemComponent extends AbstractGuiComponent implements Selectable {

    public static final String TYPE = "toggle";
    private final ParserContext options;
    private @Nullable GuiComponent[] selectionModel;
    private @NotNull String[] valueSelectionModel;
    private int selected;

    public ToggleItemComponent(int offsetX,
                               int offsetY,
                               int width,
                               int height,
                               @NotNull RenderPriority priority,
                               @NotNull String id,
                               @NotNull List<GuiFunction> clickFunctions,
                               @NotNull List<GuiFunction> dragFunctions,
                               @NotNull List<GuiFunction> closeFunctions,
                               boolean hidden,
                               ParserContext options,
                               int selected) {
        super(offsetX, offsetY, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden);
        this.options = options;
        this.selected = selected;
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        gen.writePOJOField("option", getOptions());
        gen.writeNumberField("selected", getSelected());
    }

    @Override
    public void onViewInit(@NotNull Map<String, String> placeholders) {
        selectionModel = createSelectionModel();
    }

    private @NotNull GuiComponent[] createSelectionModel() {
        if (context == null || api == null)
            return null;

        List<ParserContext> nodes = JsonUtils.getFieldList(options);
        List<GuiComponent> components = new ArrayList<>();
        List<String> selectionModel = new ArrayList<>();
        try {
            for (ParserContext node : nodes) {
                selectionModel.add(Objects.requireNonNull(node.get("value")).node().textValue());
                XmlComponent xmlComponent = context.interpreter().xmlComponent(node.node(), api);
                GuiComponent component = context.interpreter().createComponent(xmlComponent, api, context);
                components.add(component);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        valueSelectionModel = selectionModel.toArray(String[]::new);
        return components.toArray(GuiComponent[]::new);
    }

    @Override
    public void clickedComponent(@NotNull InventoryClickEvent event) {
        super.clickedComponent(event);

        selected = (selected + 1) % selectionModel.length;
        if (context == null)
            return;

        context.render();
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer, @NotNull ItemStack[][] inventory) {
        ItemStack[][] virtualInventory = new ItemStack[1][1];
        selectionModel[selected].render(viewer, virtualInventory);
        inventory[offsetY][offsetX] = virtualInventory[0][0];
    }

    public ParserContext getOptions() {
        return options;
    }

    @Override
    public String[] getSelectionModel() {
        return valueSelectionModel;
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public void setSelected(int selected) {
        this.selected = selected;
    }
}
