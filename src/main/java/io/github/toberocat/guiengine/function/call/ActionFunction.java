package io.github.toberocat.guiengine.function.call;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.toberocore.action.Actions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Custom GUI function to call an action when triggered.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ActionFunction.Deserializer.class)
public record ActionFunction(@NotNull String action) implements GuiFunction {
    public static final String ID = "action";

    /**
     * Calls the specified action using the provided API and context.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the action is called.
     */
    @Override
    public void call(@NotNull GuiEngineApi api, @NotNull GuiContext context) {
        Player viewer = context.viewer();
        if (null == viewer) return;

        new Actions(action).localActions(context.getLocalActions()).run(viewer);
    }

    /**
     * Custom deserializer to convert JSON data into an `ActionFunction` instance.
     */
    protected static class Deserializer extends JsonDeserializer<ActionFunction> {
        @Override
        public @NotNull ActionFunction deserialize(@NotNull JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new ActionFunction(node.get("").textValue());
        }
    }
}
