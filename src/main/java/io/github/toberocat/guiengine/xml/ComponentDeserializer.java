package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.render.RenderPriority;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Custom JSON deserializer for {@link XmlComponent}.
 * This class is responsible for deserializing JSON data into a {@link XmlComponent} object.
 * It overrides the {@link JsonDeserializer#deserialize(JsonParser, DeserializationContext)} method.
 * The expected JSON structure for deserialization should include "type", "priority", and "id" fields,
 * along with any other custom fields that represent data for the specific XmlComponent.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public class ComponentDeserializer extends JsonDeserializer<XmlComponent> {

    /**
     * Deserialize JSON data into a {@link XmlComponent} object.
     * The JSON data should include type, priority, and id fields,
     * along with any other custom fields that represent data for the specific XmlComponent.
     *
     * @param p    The {@link JsonParser} to read the JSON data from.
     * @param ctxt The {@link DeserializationContext} to use during deserialization.
     * @return A {@link XmlComponent} object created from the JSON data.
     * @throws IOException If an I/O error occurs during JSON parsing.
     */
    @Override
    public XmlComponent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Map<String, JsonNode> data = new HashMap<>();
        JsonNode node = p.getCodec().readTree(p);

        Iterator<String> fields = node.fieldNames();
        String type = node.get("type").textValue();
        RenderPriority priority = node.has("priority")
                ? RenderPriority.valueOf(node.get("priority").textValue())
                : RenderPriority.NORMAL;

        while (fields.hasNext()) {
            String field = fields.next();
            JsonNode jsonNode = node.get(field);
            data.put(field, jsonNode);
        }

        JsonNode id = node.get("id");
        String componentId = id == null ? UUID.randomUUID().toString() : id.textValue();

        return new XmlComponent(data, priority, type, componentId);
    }
}
