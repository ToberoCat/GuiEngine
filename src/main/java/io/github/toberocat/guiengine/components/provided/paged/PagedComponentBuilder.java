package io.github.toberocat.guiengine.components.provided.paged;

import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponentBuilder;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;

/**
 * A builder class for creating PagedComponent instances.
 * <p>
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class PagedComponentBuilder extends EmbeddedGuiComponentBuilder<PagedComponentBuilder> {
    private int showingPage;
    private int[] pattern = new int[0];
    private @Nullable ParserContext parent;


    /**
     * Sets the pattern of slots on the GUI to arrange the components.
     *
     * @param pattern The pattern of slots as an integer array.
     * @return The builder instance.
     */
    public @NotNull PagedComponentBuilder setPattern(int[] pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * Sets the parent parser context containing the GUI definition.
     *
     * @param parent The parent parser context.
     * @return The builder instance.
     */
    public @NotNull PagedComponentBuilder setParent(@NotNull ParserContext parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Sets the index of the currently showing page.
     *
     * @param showingPage The index of the currently showing page.
     * @return The builder instance.
     */
    public @NotNull PagedComponentBuilder setShowingPage(int showingPage) {
        this.showingPage = showingPage;
        return this;
    }

    @Override
    public @NotNull PagedComponent createComponent() {
        assert null != parent;
        assert null != targetGui;
        return new PagedComponent(x, y, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden, targetGui, copyAir, interactions, parent, pattern, showingPage);
    }

    @Override
    public void deserialize(@NotNull ParserContext node) throws IOException {
        deserialize(node, false);
        setShowingPage(node.getOptionalInt("showing-page").orElse(0));
        setParent(node);
        setPattern(node.getOptionalString("pattern").map(x -> Arrays.stream(x.split(","))
                .mapToInt(Integer::parseInt).toArray()).orElse(new int[0]));
    }
}