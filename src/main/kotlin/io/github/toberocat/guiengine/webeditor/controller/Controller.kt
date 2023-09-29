package io.github.toberocat.guiengine.webeditor.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import spark.Request
import spark.Spark

private val mapper = ObjectMapper().registerKotlinModule()

interface Controller {
    fun <T> getRequest(route: String, callback: (req: Request) -> T) {
        Spark.get(route) { req, res ->
            res.type("application/json")
            return@get mapper.writeValueAsString(callback.invoke(req))
        }
    }
}