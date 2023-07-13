package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 10.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiComponentBuilder {
    @NotNull GuiComponent createComponent();

    interface Factory<B extends GuiComponentBuilder> {
        @NotNull B createBuilder();
        void deserialize(@NotNull JsonNode node, @NotNull B builder) throws IOException;
    }
}
