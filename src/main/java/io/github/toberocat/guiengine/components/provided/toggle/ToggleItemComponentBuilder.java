package io.github.toberocat.guiengine.components.provided.toggle;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Builder class for creating instances of {@link ToggleItemComponent}.
 */
public class ToggleItemComponentBuilder extends AbstractGuiComponentBuilder<ToggleItemComponentBuilder> {

    private @Nullable ParserContext options;
    private int selected;

    /**
     * Set the options for the toggle item component.
     *
     * @param options The parser context containing the options for the toggle item.
     * @return This builder instance.
     */
    public @NotNull ToggleItemComponentBuilder setOptions(@NotNull ParserContext options) {
        this.options = options;
        return this;
    }

    /**
     * Set the index of the initially selected option.
     *
     * @param selected The index of the initially selected option.
     * @return This builder instance.
     */
    public @NotNull ToggleItemComponentBuilder setSelected(int selected) {
        this.selected = selected;
        return this;
    }

    /**
     * Create a new instance of {@link ToggleItemComponent} with the provided parameters.
     *
     * @return The created ToggleItemComponent instance.
     * @throws AssertionError if options are not set.
     */
    @Override
    public @NotNull ToggleItemComponent createComponent() {
        assert options != null; // Ensure options are set before creating the component.
        return new ToggleItemComponent(x, y, 1, 1, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden, options, selected);
    }

    /**
     * Deserialize the builder from a parser context node.
     *
     * @param node The parser context node containing the serialized data.
     * @throws IOException if there is an error while deserializing.
     */
    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);
        setOptions(node.getOptionalNode("option").orElseThrow(() ->
                new MissingRequiredParamException(this, "option")));
        setSelected(node.getOptionalInt("selected").orElseThrow(() ->
                new MissingRequiredParamException(this, "selected")));
    }
}
