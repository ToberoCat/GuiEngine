package io.github.toberocat.guiengine.xml.parsing

import io.github.toberocat.guiengine.components.AbstractGuiComponentBuilder
import io.github.toberocat.guiengine.exception.GuiException
import io.github.toberocat.guiengine.exception.MissingRequiredParamException


data class ParsingOptional<T>(
    private val fieldName: String,
    private val value: T?
) {

    fun optional(def: T): T = value ?: def
    fun nullable(def: T?): T? = value ?: def

    fun require(builder: AbstractGuiComponentBuilder<*>): T =
        require { MissingRequiredParamException(builder.id, builder.javaClass, fieldName) }

    fun require(id: String, clazz: Class<*>): T =
        require { MissingRequiredParamException(id, clazz, fieldName) }

    fun require(supplier: () -> GuiException): T = value ?: throw supplier.invoke()
    fun <R> map(callback: (value: T) -> R?): ParsingOptional<R> =
        ParsingOptional(fieldName, value?.let { callback(it) })

    fun <R> mapSafe(callback: (value: T) -> R?): ParsingOptional<R> = map {
        return@map try {
            callback(it)
        } catch (e: Exception) {
            null
        }
    }

    fun <R> mapSafe(callback: (value: T) -> R?, exceptionSupplier: (value: T) -> GuiException): ParsingOptional<R> =
        map {
            try {
                return@map callback(it)
            } catch (e: Exception) {
                throw exceptionSupplier.invoke(it)
            }
        }

    fun present() = value != null
}