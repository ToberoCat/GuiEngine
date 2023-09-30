package io.github.toberocat.guiengine.webeditor.controller

import io.github.toberocat.guiengine.GuiEngineApi
import spark.Spark

class GuiEngineApiController : Controller {
    fun register() {
        getRequest("/apis") { GuiEngineApi.APIS.keys }
        getRequest("/guis/:api") { GuiEngineApi.APIS[it.params("api")]?.getAvailableGuis() ?: emptySet() }
        Spark.get("/guis/:api/:gui") { req, res ->
            res.type("application/xml")
            return@get getXmlGui(req.params("api"), req.params("gui"))
        }
    }

    private fun getXmlGui(apiId: String, guiId: String): String {
        val api = GuiEngineApi.APIS[apiId] ?: return ""
        return api.xmlMapper.writeValueAsString(api.loadXmlGui(emptyMap(), guiId))
    }
}