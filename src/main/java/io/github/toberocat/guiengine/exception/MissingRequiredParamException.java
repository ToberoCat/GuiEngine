package io.github.toberocat.guiengine.exception;

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an exception that occurs when a required parameter is missing while generating a GUI component.
 * <p>
 * Created: 13.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class MissingRequiredParamException extends InvalidGuiComponentException {

    /**
     * Constructs a new `MissingRequiredParamException` with the specified information.
     *
     * @param builder       The `AbstractGuiComponentBuilder` instance that is missing the required parameter.
     * @param parameterName The name of the missing required parameter.
     */
    public MissingRequiredParamException(@NotNull AbstractGuiComponentBuilder<?> builder, @NotNull String parameterName) {
        super(String.format("Component(%s), being generated with %s is missing the required argument '%s'", builder.getId(), builder.getClass().getSimpleName(), parameterName));
    }
}
