package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.render.RenderPriority;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents an XML component to be deserialized from JSON using Jackson.
 * This class defines a record to store data for an XML component. It is annotated with
 * {@link JsonDeserialize} to use the {@link ComponentDeserializer} for deserialization.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
@JsonDeserialize(using = ComponentDeserializer.class)
public record XmlComponent(@NotNull Map<String, JsonNode> fields, @NotNull RenderPriority renderPriority,
                           @NotNull String type, @NotNull String id) {

    /**
     * Convert the fields of this  to a map of objects.
     * This method is used to transform the stored JSON data to a map of objects,
     * where each field's value is converted to its corresponding Java object representation.
     * The method also adds additional properties like "id", "type", and "renderPriority" to the map.
     *
     * @param api         The {@link GuiEngineApi} instance to use for object conversion.
     * @param transformer The {@link Function} to apply to each JSON value before conversion to an object.
     * @return A map containing the fields of this  as Java objects, including additional properties.
     */
    public @NotNull Map<String, Object> objectFields(@NotNull GuiEngineApi api, @NotNull Function<JsonNode, JsonNode> transformer) {
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
