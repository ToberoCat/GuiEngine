package io.github.toberocat.guiengine.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * Created: 07.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class JsonUtils {

    public static void writeArray(@NotNull JsonGenerator gen,
                                  @NotNull String field,
                                  @NotNull Object[] array) throws IOException {
        gen.writeArrayFieldStart(field);
        for (Object element : array)
            gen.writeObject(element);
        gen.writeEndArray();
    }

    public static void writeArray(@NotNull JsonGenerator gen,
                                  @NotNull String field,
                                  int[] array) throws IOException {
        gen.writeArrayFieldStart(field);
        for (int element : array)
            gen.writeNumber(element);
        gen.writeEndArray();
    }

    public static void writeArray(@NotNull JsonGenerator gen,
                                  @NotNull String field,
                                  @NotNull List<Object> array) throws IOException {
        gen.writeArrayFieldStart(field);
        for (Object element : array)
            gen.writeObject(element);
        gen.writeEndArray();
    }

    public static @NotNull Optional<JsonNode> getOptionalNode(@NotNull JsonNode node,
                                                              @NotNull String field) {
        return Optional.ofNullable(node.get(field));
    }

    /**
     * Read a node's children as list.
     * Example:
     * This json {"component":{"type":"item"}
     * would get parsed into a list, that would look like this:
     * [{"type":"item"}]
     * <p>
     * This json {"component":[{"type":"item"}, {"type":"head"}}]}
     * would get parsed into
     * [{"type":"item"}, {"type":"head"}]
     *
     * @param node  The parent node
     * @return An optional list with the children
     */
    public static @NotNull List<JsonNode> getFieldList(@NotNull JsonNode node) {
        List<JsonNode> children = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode n : node)
                children.add(n);
        } else {
            children.add(node);
        }

        return children;
    }

    public static @NotNull Optional<List<JsonNode>> getOptionalFieldList(@NotNull JsonNode node,
                                                                         @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            List<JsonNode> children = new ArrayList<>();
            if (x.isArray()) {
                for (JsonNode n : x)
                    children.add(n);
            } else {
                children.add(x);
            }

            return children;
        });
    }

    public static @NotNull Optional<Material> getOptionalMaterial(@NotNull JsonNode node,
                                                                  @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            try {
                return Material.valueOf(x.asText().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidGuiComponentException(String.format("The provided material '%s' doesn't match any materials", x.asText()));
            }
        });
    }

    public static @NotNull Optional<RenderPriority> getOptionalRenderPriority(@NotNull JsonNode node,
                                                                              @NotNull String field) {
        return getOptionalNode(node, field).map(x -> RenderPriority.valueOf(x.asText()));
    }

    public static @NotNull Optional<String> getOptionalString(@NotNull JsonNode node,
                                                              @NotNull String field) {
        return getOptionalNode(node, field).map(JsonNode::asText);
    }

    public static @NotNull Optional<UUID> getOptionalUUID(@NotNull JsonNode node,
                                                          @NotNull String field) {
        return getOptionalNode(node, field).map(JsonNode::asText).map(UUID::fromString);
    }

    public static @NotNull Optional<Boolean> getOptionalBoolean(@NotNull JsonNode node,
                                                                @NotNull String field) {
        return getOptionalNode(node, field).map(JsonNode::asBoolean);
    }

    public static @NotNull Optional<Integer> getOptionalInt(@NotNull JsonNode node,
                                                            @NotNull String field) {
        return getOptionalNode(node, field).map(JsonNode::asInt);
    }

    public static @NotNull Optional<String[]> getOptionalStringArray(@NotNull JsonNode node,
                                                                     @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            List<String> array = new LinkedList<>();
            for (JsonNode elementNode : x) {
                String elementValue = elementNode.asText();
                array.add(elementValue);
            }
            return array.toArray(String[]::new);
        });
    }

    public static @NotNull Optional<Map<String, JsonNode>> getOptionalNodeMap(@NotNull JsonNode node,
                                                                              @NotNull String field) {
        return getOptionalNode(node, field).map(root -> {
            Map<String, JsonNode> map = new HashMap<>();

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = root.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> n = fieldsIterator.next();
                map.put(n.getKey(), n.getValue());
            }
            return map;
        });
    }

    public static @NotNull Optional<List<GuiFunction>> getFunctions(@NotNull JsonNode node,
                                                                    @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            try {
                List<GuiFunction> functions = new LinkedList<>();
                if (x.isArray()) {
                    for (JsonNode function : x)
                        functions.add(FunctionProcessor.createFunction(function));

                } else {
                    functions.add(FunctionProcessor.createFunction(x));
                }

                return functions;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
