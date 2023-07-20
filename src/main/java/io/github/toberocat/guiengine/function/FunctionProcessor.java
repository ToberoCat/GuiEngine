package io.github.toberocat.guiengine.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.utils.JsonUtils;
import io.github.toberocat.guiengine.utils.ParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for processing and handling GUI functions.
 * <p>
 * Created: 29.04.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class FunctionProcessor {
    private static final Pattern pattern = Pattern.compile("\\{([^}]*)}", Pattern.MULTILINE);
    public static final Map<String, Class<? extends GuiFunction>> FUNCTIONS = new HashMap<>();
    public static final Set<ComputeFunction> COMPUTE_FUNCTIONS = new HashSet<>();
    public static final ObjectMapper OBJECT_MAPPER = new XmlMapper();

    /**
     * Registers a custom GUI function.
     *
     * @param id    The ID of the function.
     * @param clazz The class implementing the custom GUI function.
     */
    public static void registerFunction(@NotNull String id, @NotNull Class<? extends GuiFunction> clazz) {
        FUNCTIONS.put(id, clazz);
    }

    /**
     * Registers a custom compute function.
     *
     * @param computeFunction The custom compute function to register.
     */
    public static void registerComputeFunction(@NotNull ComputeFunction computeFunction) {
        COMPUTE_FUNCTIONS.add(computeFunction);
    }

    /**
     * Creates a GUI function from a JSON node.
     *
     * @param node The JSON node containing the function information.
     * @return The created `GuiFunction` instance, or null if the function could not be created.
     * @throws JsonProcessingException If there is an issue with processing the JSON node.
     */
    public static @Nullable GuiFunction createFunction(@NotNull JsonNode node) throws JsonProcessingException {
        String id = JsonUtils.getOptionalString(new ParserContext(node, null, null), "type").orElse(null);
        if (id == null) return null;
        return OBJECT_MAPPER.treeToValue(node, FUNCTIONS.get(id));
    }

    /**
     * Calls a collection of GUI functions with the specified API and context.
     *
     * @param functions The collection of GUI functions to call.
     * @param api       The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context   The `GuiContext` instance representing the GUI context on which the functions are called.
     */
    public static void callFunctions(@NotNull Collection<GuiFunction> functions, @NotNull GuiEngineApi api, @NotNull GuiContext context) {
        for (GuiFunction function : functions)
            function.call(api, context);
    }

    /**
     * Applies to compute functions to the provided value, replacing placeholders with their computed values.
     *
     * @param api     The `GuiEngineApi` instance used to interact with the GUI engine.
     * @param context The `GuiContext` instance representing the GUI context for which the computation is performed.
     * @param value   The input value containing placeholders to compute.
     * @return The computed value with placeholders replaced by their computed values.
     */
    public static @NotNull String applyFunctions(@NotNull GuiEngineApi api, @NotNull GuiContext context, @NotNull String value) {
        StringBuilder buffer = new StringBuilder();
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String group = matcher.group(1);
            for (ComputeFunction function : COMPUTE_FUNCTIONS) {
                if (!function.checkForFunction(group)) continue;
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(function.compute(api, context, group)));
                break;
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
