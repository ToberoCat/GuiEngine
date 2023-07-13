package io.github.toberocat.guiengine.components.provided.toggle;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.MissingRequiredParamException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static io.github.toberocat.guiengine.utils.JsonUtils.getOptionalInt;
import static io.github.toberocat.guiengine.utils.JsonUtils.getOptionalNode;


public class ToggleItemComponentBuilder extends AbstractGuiComponentBuilder<ToggleItemComponentBuilder> {

    private @Nullable JsonNode options;
    private int selected;

    public @NotNull ToggleItemComponentBuilder setOptions(@NotNull JsonNode options) {
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
    public void deserialize(@NotNull JsonNode node) throws IOException {
        super.deserialize(node);
        setOptions(getOptionalNode(node, "option").orElseThrow(() ->
                new MissingRequiredParamException(this, "option")));
        setSelected(getOptionalInt(node, "selected").orElseThrow(() ->
                new MissingRequiredParamException(this, "selected")));
    }
}