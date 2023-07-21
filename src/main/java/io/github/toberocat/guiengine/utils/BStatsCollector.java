package io.github.toberocat.guiengine.utils;

import io.github.toberocat.guiengine.GuiEngineApiPlugin;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class BStatsCollector {
    public BStatsCollector(@NotNull GuiEngineApiPlugin plugin) {
        Metrics metrics = new Metrics(plugin, 19167);
    }
}
