package io.github.toberocat.guiengine.exception

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder

/**
 * Represents an exception that occurs when a required parameter is missing while generating a GUI component.
 *
 *
 * Created: 13.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class MissingRequiredParamException(builder: AbstractGuiComponentBuilder<*>, parameterName: String) :
    InvalidGuiComponentException(
        String.format(
            "Component(%s), being generated with %s is missing the required argument '%s'",
            builder.id,
            builder.javaClass.getSimpleName(),
            parameterName
        )
    )
