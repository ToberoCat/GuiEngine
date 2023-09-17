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
        "Element $id, being generated with ${clazz.simpleName} is missing the required argument $parameterName"
    )
