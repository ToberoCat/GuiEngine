package io.github.toberocat.guiengine.exception;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 13.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class MissingRequiredParamException extends InvalidGuiComponentException {

    public MissingRequiredParamException(@NotNull AbstractGuiComponentBuilder<?> builder,
                                         @NotNull String parameterName) {
        super(String.format("Component(%s), being generated with %s is missing the required argument '%s'",
                builder.getId(), builder.getClass().getSimpleName(), parameterName));
    }
}
