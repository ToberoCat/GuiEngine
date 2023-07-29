package io.github.toberocat.guiengine.components.provided.toggle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.components.AbstractGuiComponent;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.Selectable;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.GeneratorContext;
import io.github.toberocat.guiengine.utils.ParserContext;
import io.github.toberocat.guiengine.xml.XmlComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a toggle item component in a GUI. A toggle item can have multiple options, and clicking on the component
 * cycles through these options.
 * <p>
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ToggleItemComponent extends AbstractGuiComponent implements Selectable {

    /**
     * The type identifier for a ToggleItemComponent.
     */
    public static final String TYPE = "toggle";
    private final ParserContext options;
    private @Nullable GuiComponent @Nullable [] selectionModel;
    private @NotNull String[] valueSelectionModel;
    private int selected;

    /**
     * Constructs a ToggleItemComponent with the specified parameters.
     *
     * @param offsetX        The X-coordinate offset of the component's position in the GUI.
     * @param offsetY        The Y-coordinate offset of the component's position in the GUI.
     * @param width          The width of the component.
     * @param height         The height of the component.
     * @param priority       The render priority of the component.
     * @param id             The unique identifier of the component.
     * @param clickFunctions A list of GUI functions to be executed when the component is clicked.
     * @param dragFunctions  A list of GUI functions to be executed when the component is dragged.
     * @param closeFunctions A list of GUI functions to be executed when the component is closed.
     * @param hidden         Whether the component is initially hidden or not.
     * @param options        The parser context containing the options for the toggle item.
     * @param selected       The index of the initially selected option.
     */
    public ToggleItemComponent(int offsetX, int offsetY, int width, int height, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, boolean hidden, ParserContext options, int selected) {
        super(offsetX, offsetY, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden);
        this.options = options;
        this.selected = selected;
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        gen.writePOJOField("option", options);
        gen.writeNumberField("selected", selected);
    }

    @Override
    public void onViewInit(@NotNull Map<String, String> placeholders) {
        selectionModel = createSelectionModel();
    }

    /**
     * Creates the selection model for the toggle item from the provided options in the parser context.
     *
     * @return The selection model as an array of GuiComponents.
     */
    private GuiComponent @Nullable [] createSelectionModel() {
        if (null == context || null == api) return null;

        List<ParserContext> nodes = options.getFieldList();
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
        if (null == context) return;

        context.render();
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer, @NotNull ItemStack[] @NotNull [] inventory) {
        ItemStack[][] virtualInventory = new ItemStack[1][1];
        selectionModel[selected].render(viewer, virtualInventory);
        inventory[offsetY][offsetX] = virtualInventory[0][0];
    }

    /**
     * Get the parser context containing the options for the toggle item.
     *
     * @return The parser context.
     */
    public ParserContext getOptions() {
        return options;
    }

    @Override
    public String[] getSelectionModel() {
        return valueSelectionModel;
    }

    /**
     * Get the index of the currently selected option.
     *
     * @return The index of the selected option.
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Set the index of the currently selected option.
     *
     * @param selected The index of the option to set as selected.
     */
    @Override
    public void setSelected(int selected) {
        this.selected = selected;
    }
}
