package io.github.toberocat.guiengine.api.components.provided.toggle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.JsonUtils;
import io.github.toberocat.guiengine.api.xml.XmlComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ToggleItemComponent extends AbstractGuiComponent {

    public static final String TYPE = "toggle";
    private final JsonNode options;
    private @Nullable GuiComponent[] selectionModel;
    private int selected;

    public ToggleItemComponent(@NotNull RenderPriority priority,
                               @NotNull String id,
                               @NotNull JsonNode options,
                               int selected,
                               int x,
                               int y) {
        super(id, x, y, 1, 1, priority);
        this.options = options;
        this.selected = selected;
    }

    @Override
    public void onViewInit(@NotNull Map<String, String> placeholders) {
        selectionModel = createSelectionModel();
    }

    private @NotNull GuiComponent[] createSelectionModel() {
        if (context == null || api == null)
            return null;

        List<JsonNode> nodes = JsonUtils.getFieldList(options);
        List<GuiComponent> components = new ArrayList<>();
        try {
            for (JsonNode node : nodes) {
                XmlComponent xmlComponent = context.interpreter().xmlComponent(node, api);
                GuiComponent component = context.interpreter().createComponent(xmlComponent, api, context);
                components.add(component);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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

    public JsonNode getOptions() {
        return options;
    }

    public int getSelected() {
        return selected;
    }
}
