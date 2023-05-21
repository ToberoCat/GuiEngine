package io.github.toberocat.guiengine.api.components.provided.item;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import io.github.toberocat.guiengine.api.function.FunctionProcessor;
import io.github.toberocat.guiengine.api.function.GuiFunction;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import io.github.toberocat.guiengine.api.utils.JsonUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.github.toberocat.guiengine.api.utils.JsonUtils.*;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class SimpleItemDeserializer extends JsonDeserializer<SimpleItemComponent> {
    @Override
    public SimpleItemComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        return new SimpleItemComponentBuilder()
                .setPriority(getOptionalRenderPriority(node, "priority").orElse(RenderPriority.NORMAL))
                .setId(getOptionalString(node, "id").orElseThrow())
                .setName(getOptionalString(node, "name").orElse(" "))
                .setMaterial(getOptionalMaterial(node, "material").orElseThrow())
                .setLore(getOptionalStringArray(node, "lore").orElse(new String[0]))
                .setClickFunctions(getFunctions(node, "on-click").orElse(new ArrayList<>()))
                .setX(getOptionalInt(node, "x").orElse(0))
                .setY(getOptionalInt(node, "y").orElse(0))
                .setHidden(getOptionalBoolean(node, "hidden").orElse(false))
                .createSimpleItemComponent();
    }
}
