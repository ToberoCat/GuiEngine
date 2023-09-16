package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import org.bukkit.Bukkit
import java.io.IOException
import java.util.concurrent.TimeUnit

@JsonDeserialize(using = DelayFunction.Deserializer::class)
data class DelayFunction(val unit: TimeUnit, val duration: Int) : GuiFunction {
    override val type = TYPE
    override fun call(context: GuiContext) {
        try {
            Thread.sleep(unit.toMillis(duration.toLong()))
        } catch (e: InterruptedException) {
            Bukkit.getLogger().warning(
                String.format(
                    "Failed to wait for %s ms while executing gui functions", unit.toMillis(
                        duration.toLong()
                    )
                )
            )
        }
    }

    /**
     * Custom deserializer to convert JSON data into an `ActionFunction` instance.
     */
    class Deserializer : JsonDeserializer<DelayFunction>() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): DelayFunction {
            val node = p.codec.readTree<JsonNode>(p)
            val context = ParserContext.empty(node)
            return DelayFunction(
                context.enum(TimeUnit::class.java, "unit")
                    .optional(TimeUnit.SECONDS),
                node[""].asInt()
            )
        }
    }

    companion object {
        const val TYPE = "delay"
    }
}
