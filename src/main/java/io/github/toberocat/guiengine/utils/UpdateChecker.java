package io.github.toberocat.guiengine.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * A utility class for checking updates for a Bukkit/Spigot plugin.
 * It retrieves the latest version information from the SpigotMC API asynchronously.
 */
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    /**
     * Create a new instance of the UpdateChecker.
     *
     * @param plugin     The JavaPlugin instance of the plugin.
     * @param resourceId The resource ID of the plugin on SpigotMC.
     */
    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    /**
     * Get the latest version information from the SpigotMC API.
     *
     * @param consumer A consumer that will receive the latest version information as a String.
     */
    public void getVersion(final @NotNull Consumer<String> consumer) {
        // Run the version check task asynchronously to avoid blocking the main thread.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream(); Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                // The API returns the latest version as a single line of text.
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                // Handle any exceptions that might occur during the version check.
                plugin.getLogger().info("Unable to check for updates");
            }
        });
    }
}
