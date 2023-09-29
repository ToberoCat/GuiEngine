package io.github.toberocat.guiengine.function.call

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.toberocat.guiengine.context.GuiContext
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.function.GuiFunctionFactory
import io.github.toberocat.guiengine.xml.parsing.ParserContext
import org.bukkit.Bukkit
import java.util.concurrent.TimeUnit

@JsonDeserialize(using = DelayFunction.Deserializer::class)
data class DelayFunction(val unit: TimeUnit, @JsonProperty("$") val duration: Int) : GuiFunction {
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
    class Deserializer : GuiFunctionFactory<DelayFunction>() {

        override fun build(node: ParserContext) = DelayFunction(
            node.enum(TimeUnit::class.java, "unit").optional(TimeUnit.SECONDS), node.int("$").require(TYPE, javaClass)
        )
    }

    companion object {
        const val TYPE = "delay"
    }
}
