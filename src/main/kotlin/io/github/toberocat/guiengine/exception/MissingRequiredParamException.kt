package io.github.toberocat.guiengine.exception

/**
 * Represents an exception that occurs when a required parameter is missing while generating a GUI component.
 *
 *
 * Created: 13.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class MissingRequiredParamException(id: String, clazz: Class<*>, parameterName: String) :
    InvalidGuiComponentException(
        String.format(
            "Element %s, being generated with %s is missing the required argument '%s'",
            id,
            clazz.getSimpleName(),
            parameterName
        )
    )
