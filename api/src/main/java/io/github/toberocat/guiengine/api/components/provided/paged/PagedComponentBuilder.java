package io.github.toberocat.guiengine.api.components.provided.paged;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.api.components.provided.embedded.EmbeddedGuiComponentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.getOptionalInt;
import static io.github.toberocat.guiengine.api.utils.JsonUtils.getOptionalString;

public class PagedComponentBuilder extends EmbeddedGuiComponentBuilder<PagedComponentBuilder> {
    private int showingPage = 0;
    private int[] pattern = new int[0];
    private @Nullable JsonNode parent;


    public @NotNull PagedComponentBuilder setPattern(int[] pattern) {
        this.pattern = pattern;
        return this;
    }

    public @NotNull PagedComponentBuilder setParent(@NotNull JsonNode parent) {
        this.parent = parent;
        return this;
    }

    public @NotNull PagedComponentBuilder setShowingPage(int showingPage) {
        this.showingPage = showingPage;
        return this;
    }

    @Override
    public @NotNull PagedComponent createComponent() {
        assert parent != null;
        assert targetGui != null;
        return new PagedComponent(x,
                y,
                width,
                height,
                priority,
                id,
                clickFunctions,
                dragFunctions,
                closeFunctions,
                hidden,
                targetGui,
                copyAir,
                interactions,
                parent,
                pattern,
                showingPage);
    }

    public static class Factory extends EmbeddedGuiComponentBuilder.Factory<PagedComponentBuilder> {
        @Override
        public @NotNull PagedComponentBuilder createBuilder() {
            return new PagedComponentBuilder();
        }

        @Override
        public void deserialize(@NotNull JsonNode node, @NotNull PagedComponentBuilder builder) throws IOException {
            super.deserialize(node, builder);
            builder.setShowingPage(getOptionalInt(node, "showing-page").orElse(0))
                    .setParent(node)
                    .setPattern(getOptionalString(node, "pattern")
                            .map(x -> Arrays.stream(x.split(","))
                                    .mapToInt(Integer::parseInt)
                                    .toArray())
                            .orElse(new int[0]));
        }
    }
}