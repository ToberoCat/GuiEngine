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
 * A helper class representing a context for parsing JSON data.
 * This class provides utility methods to extract values from a JSON node
 * and convert them to their corresponding data types.
 */
public record ParserContext(@NotNull JsonNode node, @NotNull GuiContext context, @NotNull GuiEngineApi api) {

    /**
     * Get a child node of the current node based on the specified field name.
     *
     * @param field The field name of the child node to retrieve.
     * @return A ParserContext representing the child node if found, or null if not present.
     */
    public @Nullable ParserContext get(String field) {
        JsonNode n = node.get(field);
        if (null == n) return null;
        return new ParserContext(n, context, api);
    }

    /**
     * Get an optional child node of the current node based on the specified field name.
     *
     * @param field The field name of the optional child node to retrieve.
     * @return An Optional containing the child node if found, or an empty Optional if not present.
     */
    public @NotNull Optional<ParserContext> getOptionalNode(@NotNull String field) {
        return Optional.ofNullable(get(field));
    }

    /**
     * Read a node's children as a list of ParserContext objects.
     * If the current node is an array, each element will be represented as a separate ParserContext object.
     * If the current node is not an array, the current node itself will be the only element in the list.
     *
     * @return A list of ParserContext objects representing the node's children.
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

    /**
     * Get an optional list of ParserContext objects from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional list.
     * @return An Optional containing the list of ParserContext objects if found,
     * or an empty Optional if not present.
     */
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

    /**
     * Get an optional Material enum value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Material.
     * @return An Optional containing the Material enum value if found,
     * or an empty Optional if not present.
     * @throws InvalidGuiComponentException If the provided material doesn't match any known materials.
     */
    public @NotNull Optional<Material> getOptionalMaterial(@NotNull String field) {
        return getOptionalString(field).map(x -> {
            try {
                return Material.valueOf(x.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidGuiComponentException(String.format("The provided material '%s' doesn't match any materials", x));
            }
        });
    }

    /**
     * Get an optional RenderPriority enum value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional RenderPriority.
     * @return An Optional containing the RenderPriority enum value if found,
     * or an empty Optional if not present.
     */
    public @NotNull Optional<RenderPriority> getOptionalRenderPriority(@NotNull String field) {
        return getOptionalString(field).map(RenderPriority::valueOf);
    }

    /**
     * Get an optional String value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String.
     * @return An Optional containing the String value if found,
     * or an empty Optional if not present.
     */
    public @NotNull Optional<String> getOptionalString(@NotNull String field) {
        return getOptionalNode(field).map(x -> x.node().asText()).map(x -> FunctionProcessor.applyFunctions(api, context, x));
    }

    /**
     * Get an optional UUID value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional UUID.
     * @return An Optional containing the UUID value if found,
     * or an empty Optional if not present.
     */
    public @NotNull Optional<UUID> getOptionalUUID(@NotNull String field) {
        return getOptionalString(field).map(x -> {
            try {
                return UUID.fromString(x);
            } catch (IllegalArgumentException e) {
                return null;
            }
        });
    }

    /**
     * Get an optional Boolean value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Boolean.
     * @return An Optional containing the Boolean value if found,
     * or an empty Optional if not present.
     */
    public @NotNull Optional<Boolean> getOptionalBoolean(@NotNull String field) {
        return getOptionalString(field).map(x -> "true".equals(x));
    }

    /**
     * Get an optional Integer value from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional Integer.
     * @return An Optional containing the Integer value if found,
     * or an empty Optional if not present.
     */
    public @NotNull Optional<Integer> getOptionalInt(@NotNull String field) {
        return getOptionalString(field).map(Integer::parseInt);
    }

    /**
     * Get an optional array of Strings from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional String array.
     * @return An Optional containing the array of Strings if found,
     * or an empty Optional if not present.
     */
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

    /**
     * Get an optional map of String keys and ParserContext values from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional node map.
     * @return An Optional containing the map of String keys and ParserContext values if found,
     * or an empty Optional if not present.
     */
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

    /**
     * Get an optional list of GuiFunction objects from the child node of the current node
     * based on the specified field name.
     *
     * @param field The field name of the optional functions.
     * @return An Optional containing the list of GuiFunction objects if found,
     * or an empty Optional if not present.
     */
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
