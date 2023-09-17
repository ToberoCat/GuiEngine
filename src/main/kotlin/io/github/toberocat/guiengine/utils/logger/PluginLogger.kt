package io.github.toberocat.guiengine.utils.logger

import java.util.logging.Logger

class PluginLogger(private val logger: Logger) : GuiLogger {
    override fun info(message: String) = logger.info(message)

    override fun debug(message: String) = logger.fine(message)

    override fun error(message: String?) = logger.severe(message)
}