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

    public static void writeArray(@NotNull GeneratorContext gen,
                                  @NotNull String field,
                                  @NotNull Object[] array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (Object element : array)
            generator.writeObject(element);
        generator.writeEndArray();
    }

    public static void writeArray(@NotNull GeneratorContext gen,
                                  @NotNull String field,
                                  int[] array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (int element : array)
            generator.writeNumber(element);
        generator.writeEndArray();
    }

    public static void writeArray(@NotNull GeneratorContext gen,
                                  @NotNull String field,
                                  @NotNull List<Object> array) throws IOException {
        JsonGenerator generator = gen.generator();
        generator.writeArrayFieldStart(field);
        for (Object element : array)
            generator.writeObject(element);
        generator.writeEndArray();
    }

    public static @NotNull Optional<ParserContext> getOptionalNode(@NotNull ParserContext node,
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
     * @param node The parent node
     * @return An optional list with the children
     */
    public static @NotNull List<ParserContext> getFieldList(@NotNull ParserContext node) {
        List<ParserContext> children = new ArrayList<>();
        if (node.node().isArray()) {
            for (JsonNode n : node.node())
                children.add(new ParserContext(n, node.context(), node.api()));
        } else {
            children.add(node);
        }

        return children;
    }

    public static @NotNull Optional<List<ParserContext>> getOptionalFieldList(@NotNull ParserContext node,
                                                                         @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            List<ParserContext> children = new ArrayList<>();
            if (x.node().isArray()) {
                for (JsonNode n : x.node())
                    children.add(new ParserContext(n, node.context(), node.api()));
            } else {
                children.add(x);
            }

            return children;
        });
    }

    public static @NotNull Optional<Material> getOptionalMaterial(@NotNull ParserContext node,
                                                                  @NotNull String field) {
        return getOptionalString(node, field).map(x -> {
            try {
                return Material.valueOf(x.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidGuiComponentException(String.format("The provided material '%s' doesn't match any materials", x));
            }
        });
    }

    public static @NotNull Optional<RenderPriority> getOptionalRenderPriority(@NotNull ParserContext node,
                                                                              @NotNull String field) {
        return getOptionalString(node, field).map(RenderPriority::valueOf);
    }

    public static @NotNull Optional<String> getOptionalString(@NotNull ParserContext node,
                                                              @NotNull String field) {
        return getOptionalNode(node, field).map(x -> x.node().asText());
    }

    public static @NotNull Optional<UUID> getOptionalUUID(@NotNull ParserContext node,
                                                          @NotNull String field) {
        return getOptionalNode(node, field).map(x -> x.node().asText()).map(UUID::fromString);
    }

    public static @NotNull Optional<Boolean> getOptionalBoolean(@NotNull ParserContext node,
                                                                @NotNull String field) {
        return getOptionalNode(node, field).map(x -> x.node().asBoolean());
    }

    public static @NotNull Optional<Integer> getOptionalInt(@NotNull ParserContext node,
                                                            @NotNull String field) {
        return getOptionalNode(node, field).map(x -> x.node().asInt());
    }

    public static @NotNull Optional<String[]> getOptionalStringArray(@NotNull ParserContext node,
                                                                     @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            List<String> array = new LinkedList<>();
            for (JsonNode elementNode : x.node()) {
                String elementValue = elementNode.asText();
                array.add(elementValue);
            }
            return array.toArray(String[]::new);
        });
    }

    public static @NotNull Optional<Map<String, ParserContext>> getOptionalNodeMap(@NotNull ParserContext node,
                                                                              @NotNull String field) {
        return getOptionalNode(node, field).map(root -> {
            Map<String, ParserContext> map = new HashMap<>();

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = root.node().fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> n = fieldsIterator.next();
                map.put(n.getKey(), new ParserContext(n.getValue(), node.context(), node.api()));
            }
            return map;
        });
    }

    public static @NotNull Optional<List<GuiFunction>> getFunctions(@NotNull ParserContext node,
                                                                    @NotNull String field) {
        return getOptionalNode(node, field).map(x -> {
            try {
                List<GuiFunction> functions = new LinkedList<>();
                if (x.node().isArray()) {
                    for (JsonNode function : x.node())
                        functions.add(FunctionProcessor.createFunction(function));

                } else {
                    functions.add(FunctionProcessor.createFunction(x.node()));
                }

                return functions;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
