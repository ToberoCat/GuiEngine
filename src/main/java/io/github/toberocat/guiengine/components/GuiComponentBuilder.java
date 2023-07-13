package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created: 10.07.2023
 * <p>
 * This class should make it easier to create inheritable builder patterns.
 * It also tries to NOT merge the create & deserialize function, so you're forced to use variables, making it easier to
 * create a builder pattern (As intended for easier use without requiring an interpreter)
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiComponentBuilder {
    @NotNull GuiComponent createComponent();

    void deserialize(@NotNull JsonNode node) throws IOException;
}
