package io.github.toberocat.guiengine.utils.logger

interface GuiLogger {
    fun info(message: String)
    fun debug(message: String)

    fun error(message: String?)
}