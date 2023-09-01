package io.github.toberocat.guiengine.utils

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer

/**
 * A utility class for checking updates for a Bukkit/Spigot plugin.
 * It retrieves the latest version information from the SpigotMC API asynchronously.
 */
class UpdateChecker(private val plugin: JavaPlugin, private val resourceId: Int) {
    /**
     * Get the latest version information from the SpigotMC API.
     *
     * @param consumer A consumer that will receive the latest version information as a String.
     */
    fun getVersion(consumer: Consumer<String?>) {
        // Run the version check task asynchronously to avoid blocking the main thread.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=$resourceId").openStream().use { inputStream ->
                    Scanner(inputStream, StandardCharsets.UTF_8).use { scanner ->
                        // The API returns the latest version as a single line of text.
                        if (scanner.hasNext()) {
                            consumer.accept(scanner.next())
                        }
                    }
                }
            } catch (exception: IOException) {
                // Handle any exceptions that might occur during the version check.
                plugin.logger.info("Unable to check for updates")
            }
        })
    }
}
