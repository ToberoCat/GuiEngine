package io.github.toberocat.guiengine.utils

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.InvalidGuiComponentException
import io.github.toberocat.guiengine.exception.MissingRequiredParamException
import java.util.*

fun <T> Optional<T>.orElseThrow(builder: AbstractGuiComponentBuilder<*>, fieldName: String): T =
    orElseThrow { MissingRequiredParamException(builder, fieldName) }

fun <T> T?.orElseThrow(builder: AbstractGuiComponentBuilder<*>, fieldName: String): T =
    this ?: throw MissingRequiredParamException(builder, fieldName)

fun <T> T?.orElseThrow(error: String): T =
    this ?: throw InvalidGuiComponentException("Couldn't construct gui. Caused by: $error")
