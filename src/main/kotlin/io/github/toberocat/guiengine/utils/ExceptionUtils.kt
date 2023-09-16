package io.github.toberocat.guiengine.utils

import io.github.toberocat.guiengine.exception.GuiActionException
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.toberocore.action.Action

fun <T> T?.nullCheck(error: String): T =
    this ?: throw InvalidGuiComponentException("Couldn't construct gui. Caused by: $error")

fun Action.validate(error: String, callback: () -> Boolean) {
    if (callback.invoke())
        throw GuiActionException(this, error)
}