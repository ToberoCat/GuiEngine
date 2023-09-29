package io.github.toberocat.guiengine.webeditor

import io.github.toberocat.guiengine.webeditor.controller.GuiEngineApiController
import spark.kotlin.port
import spark.kotlin.staticFiles

class WebEditorServer(port: Int) {
    init {
        port(port)
        staticFiles.location("/webeditor")
        GuiEngineApiController().register()
    }
}

fun main() {
    WebEditorServer(8080)
}