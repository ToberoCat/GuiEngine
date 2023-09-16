package io.github.toberocat.guiengine.xml.parsing

import com.fasterxml.jackson.databind.JsonNode
import io.github.toberocat.guiengine.GuiEngineApi
import io.github.toberocat.guiengine.context.GuiContext

class PlaceholderParserContext(
    node: JsonNode,
    private val placeholders: Map<String, String>,
    computables: MutableMap<String, String>,
    context: GuiContext?,
    api: GuiEngineApi?
) : ParserContext(node, computables, context, api) {
    companion object {
        fun ParserContext.toPlaceholderContext(placeholders: Map<String, String>): PlaceholderParserContext =
            PlaceholderParserContext(node, placeholders, computables, context, api)

    }

    override fun iterator(): Iterator<PlaceholderParserContext> =
        node.map { PlaceholderParserContext(it, placeholders, computables, context, api) }.iterator()

    override fun get(field: String): PlaceholderParserContext? = when {
        node.has(field) -> PlaceholderParserContext(node.get(field), placeholders, computables, context, api)
        else -> null
    }

    override fun asText(field: String, node: JsonNode): String {
        var text = super.asText(field, node)
        placeholders.forEach { (key, value) -> text = text.replace(key, value) }
        return text
    }
}