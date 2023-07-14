package io.github.toberocat.guiengine.components.provided.toggle;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public class ToggleItemComponentBuilder extends AbstractGuiComponentBuilder<ToggleItemComponentBuilder> {

    private @Nullable ParserContext options;
    private int selected;

    public @NotNull ToggleItemComponentBuilder setOptions(@NotNull ParserContext options) {
        this.options = options;
        return this;
    }

    public @NotNull ToggleItemComponentBuilder setSelected(int selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public @NotNull ToggleItemComponent createComponent() {
        assert options != null;
        return new ToggleItemComponent(x,
                y,
                1,
                1,
                priority,
                id,
                clickFunctions,
                dragFunctions,
                closeFunctions,
                hidden,
                options,
                selected);
    }

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        super.deserialize(node);
        setOptions(node.getOptionalNode("option").orElseThrow(() ->
                new MissingRequiredParamException(this, "option")));
        setSelected(node.getOptionalInt("selected").orElseThrow(() ->
                new MissingRequiredParamException(this, "selected")));
    }
}