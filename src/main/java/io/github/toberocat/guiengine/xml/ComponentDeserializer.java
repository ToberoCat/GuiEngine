package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import io.github.toberocat.guiengine.render.RenderPriority;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ComponentDeserializer extends JsonDeserializer<XmlComponent> {
    @Override
    public XmlComponent deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
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
        return new XmlComponent(data, priority, type, id == null ? UUID.randomUUID().toString() : id.textValue());
    }
}
