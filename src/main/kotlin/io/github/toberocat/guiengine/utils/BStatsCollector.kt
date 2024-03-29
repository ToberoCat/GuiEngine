package io.github.toberocat.guiengine.utils

import io.github.toberocat.guiengine.GuiEngineApiPlugin
import org.bstats.bukkit.Metrics

/**
 * BStatsCollector is a utility class responsible for collecting usage statistics using bStats.
 *
 *
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
class BStatsCollector(plugin: GuiEngineApiPlugin) {
    /**
     * Creates a new BStatsCollector and initializes the bStats metrics collection for the plugin.
     *
     * @param plugin The GuiEngineApiPlugin instance to collect metrics for.
     */
    init {
        Metrics(plugin, 19167)
    }
}
