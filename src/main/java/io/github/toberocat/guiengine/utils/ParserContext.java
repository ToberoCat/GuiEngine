package io.github.toberocat.guiengine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created: 14.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public record ParserContext(@NotNull JsonNode node,
                            @NotNull GuiContext context,
                            @NotNull GuiEngineApi api) {

    public @Nullable ParserContext get(String field) {
        JsonNode n = node.get(field);
        if (n == null)
            return null;
        return new ParserContext(n, context, api);
    }

    public @NotNull Optional<ParserContext> getOptionalNode(@NotNull String field) {
        return Optional.ofNullable(get(field));
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
     * @return An optional list with the children
     */
    public @NotNull List<ParserContext> getFieldList() {
        List<ParserContext> children = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode n : node)
                children.add(new ParserContext(n, context, api));
        } else {
            children.add(this);
        }

        return children;
    }

    public @NotNull Optional<List<ParserContext>> getOptionalFieldList(@NotNull String field) {
        return getOptionalNode(field).map(x -> {
            List<ParserContext> children = new ArrayList<>();
            if (x.node().isArray()) {
                for (JsonNode n : x.node())
                    children.add(new ParserContext(n, context, api));
            } else {
                children.add(x);
            }

            return children;
        });
    }

    public @NotNull Optional<Material> getOptionalMaterial(@NotNull String field) {
        return getOptionalString(field).map(x -> {
            try {
                return Material.valueOf(x.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidGuiComponentException(String.format("The provided material '%s' doesn't match any materials", x));
            }
        });
    }

    public @NotNull Optional<RenderPriority> getOptionalRenderPriority(@NotNull String field) {
        return getOptionalString(field).map(RenderPriority::valueOf);
    }

    public @NotNull Optional<String> getOptionalString(@NotNull String field) {
        return getOptionalNode(field)
                .map(x -> x.node().asText())
                .map(x -> FunctionProcessor.applyFunctions(api, context, x));
    }

    public @NotNull Optional<UUID> getOptionalUUID(@NotNull String field) {
        return getOptionalString(field).map(x -> {
            try {
                return UUID.fromString(x);
            } catch (IllegalArgumentException e) {
                return null;
            }
        });
    }

    public @NotNull Optional<Boolean> getOptionalBoolean(@NotNull String field) {
        return getOptionalString(field).map(x -> x.equals("true"));
    }

    public @NotNull Optional<Integer> getOptionalInt(@NotNull String field) {
        return getOptionalString(field).map(Integer::parseInt);
    }

    public @NotNull Optional<String[]> getOptionalStringArray(@NotNull String field) {
        return getOptionalNode(field).map(x -> {
            List<String> array = new LinkedList<>();
            for (JsonNode elementNode : x.node()) {
                String elementValue = elementNode.asText();
                array.add(elementValue);
            }
            return array.toArray(String[]::new);
        });
    }

    public @NotNull Optional<Map<String, ParserContext>> getOptionalNodeMap(@NotNull String field) {
        return getOptionalNode(field).map(root -> {
            Map<String, ParserContext> map = new HashMap<>();

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = root.node().fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> n = fieldsIterator.next();
                map.put(n.getKey(), new ParserContext(n.getValue(), context, api));
            }
            return map;
        });
    }

    public @NotNull Optional<List<GuiFunction>> getFunctions(@NotNull String field) {
        return getOptionalNode(field).map(x -> {
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
