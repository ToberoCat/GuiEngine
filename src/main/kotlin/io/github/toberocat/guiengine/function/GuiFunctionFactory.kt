package io.github.toberocat.guiengine.function

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import io.github.toberocat.guiengine.xml.parsing.ParserContext

abstract class GuiFunctionFactory<T : GuiFunction> : JsonDeserializer<T>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T =
        build(ParserContext.empty(p.codec.readTree(p)))

    abstract fun build(node: ParserContext): T
}