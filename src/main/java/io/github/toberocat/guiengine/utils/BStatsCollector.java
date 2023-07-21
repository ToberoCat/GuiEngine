package io.github.toberocat.guiengine.utils;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

/**
 * BStatsCollector is a utility class responsible for collecting usage statistics using bStats.
 * <p>
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class BStatsCollector {

    /**
     * Creates a new BStatsCollector and initializes the bStats metrics collection for the plugin.
     *
     * @param plugin The GuiEngineApiPlugin instance to collect metrics for.
     */
    public BStatsCollector(@NotNull GuiEngineApiPlugin plugin) {
        Metrics metrics = new Metrics(plugin, 19167);
    }
}
