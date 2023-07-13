package io.github.toberocat.guiengine.components.provided.toggle;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
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

    public static class Factory extends AbstractGuiComponentBuilder.Factory<ToggleItemComponentBuilder> {

        @Override
        public @NotNull ToggleItemComponentBuilder createBuilder() {
            return new ToggleItemComponentBuilder();
        }

        @Override
        public void deserialize(@NotNull JsonNode node, @NotNull ToggleItemComponentBuilder builder) throws IOException {
            super.deserialize(node, builder);
            builder.setOptions(getOptionalNode(node, "option").orElseThrow(() ->
                            new InvalidGuiComponentException("The component is missing a required argument 'option'")))
                    .setSelected(getOptionalInt(node, "selected").orElseThrow(() ->
                            new InvalidGuiComponentException("The component is missing a required argument 'selected'")));
        }
    }
}