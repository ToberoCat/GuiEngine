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
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ActionFunction.Deserializer.class)
public record ActionFunction(@NotNull String action) implements GuiFunction {
    public static final String ID = "action";

    @Override
    public void call(@NotNull GuiEngineApi api,
                     @NotNull GuiContext context) {
        Player viewer = context.viewer();
        if (viewer == null)
            return;

        new Actions(action)
                .localActions(context.getLocalActions())
                .run(viewer);
    }

    protected static class Deserializer extends JsonDeserializer<ActionFunction> {
        @Override
        public ActionFunction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return new ActionFunction(node.get("").textValue());
        }
    }
}
