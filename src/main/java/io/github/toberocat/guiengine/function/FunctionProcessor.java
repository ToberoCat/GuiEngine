package io.github.toberocat.guiengine.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class FunctionProcessor {
    private static final Pattern pattern = Pattern.compile("\\{([^}]*)}", Pattern.MULTILINE);
    public static final Map<String, Class<? extends GuiFunction>> FUNCTIONS = new HashMap<>();
    public static final Set<ComputeFunction> COMPUTE_FUNCTIONS = new HashSet<>();
    public static final ObjectMapper OBJECT_MAPPER = new XmlMapper();

    public static void registerFunction(@NotNull String id, @NotNull Class<? extends GuiFunction> clazz) {
        FUNCTIONS.put(id, clazz);
    }

    public static void registerComputeFunction(@NotNull ComputeFunction computeFunction) {
        COMPUTE_FUNCTIONS.add(computeFunction);
    }

    public static @Nullable GuiFunction createFunction(@NotNull JsonNode node) throws JsonProcessingException {
        String id = JsonUtils.getOptionalString(node, "type").orElse(null);
        if (id == null)
            return null;
        return OBJECT_MAPPER.treeToValue(node, FUNCTIONS.get(id));
    }

    public static void callFunctions(@NotNull Collection<GuiFunction> functions,
                                     @NotNull GuiEngineApi api,
                                     @NotNull GuiContext context) {
        for (GuiFunction function : functions)
            function.call(api, context);
    }

    public static @NotNull String applyFunctions(@NotNull GuiEngineApi api,
                                        @NotNull GuiContext context,
                                        @NotNull String value) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String group = matcher.group(1);
            for (ComputeFunction function : COMPUTE_FUNCTIONS) {
                if (!function.checkForFunction(group))
                    continue;
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(function.compute(api, context, group)));
                break;
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
