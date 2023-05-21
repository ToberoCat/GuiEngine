package io.github.toberocat.guiengine.api.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.render.RenderPriority;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ComponentDeserializer.class)
public record XmlComponent(@NotNull Map<String, JsonNode> fields,
                           @NotNull RenderPriority renderPriority,
                           @NotNull String type,
                           @NotNull String id) {
    public @NotNull Map<String, Object> objectFields(@NotNull GuiEngineApi api,
                                                     @NotNull Function<JsonNode, JsonNode> transformer) {
        Map<String, Object> objectMap = new HashMap<>();
        for (Map.Entry<String, JsonNode> entry : fields.entrySet()) {
            JsonNode prop = transformer.apply(entry.getValue());
            Object value = api.getXmlMapper().convertValue(prop, Object.class);
            objectMap.put(entry.getKey(), value);
        }
        objectMap.put("id", id);
        objectMap.put("type", type);
        objectMap.put("renderPriority", renderPriority);
        return objectMap;
    }
}
