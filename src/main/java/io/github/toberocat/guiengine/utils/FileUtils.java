package io.github.toberocat.guiengine.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * FileUtils is a utility class providing file-related operations for Java plugins.
 * <p>
 * Created: 21.07.2023
 * Author: Tobias Madlberger (Tobias)
 */
public class FileUtils {

    /**
     * Copies all files and directories from the specified root folder within the plugin's JAR file to the plugin's data folder.
     * If a folder is encountered during the copying process, it recursively copies its contents as well.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param root   The root folder within the JAR file to copy from.
     * @return The File representing the root folder in the plugin's data folder after copying.
     * @throws IOException        If an I/O error occurs during copying.
     * @throws URISyntaxException If the plugin's root URL cannot be converted to a URI.
     */
    public static @NotNull File copyAll(@NotNull JavaPlugin plugin, @NotNull String root) throws IOException, URISyntaxException {
        plugin.getDataFolder().mkdirs();
        foreachIn(plugin, root, x -> {
            if (x.toString().equals(root)) return;
            try {
                String file = root + "/" + x.getFileName();
                if (Files.isDirectory(x)) {
                    copyAll(plugin, file);
                } else {
                    copyResource(plugin, file);
                }
            } catch (IOException | URISyntaxException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stackTrace = sw.toString();

                plugin.getLogger().severe("An exception occurred: " + e.getMessage() + "\n" + stackTrace);
            }
        });
        return new File(plugin.getDataFolder(), root);
    }

    /**
     * Lists all files and directories within the specified root folder within the plugin's JAR file.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param root   The root folder within the JAR file to list from.
     * @return A list of Path objects representing the files and directories in the specified root folder.
     * @throws URISyntaxException If the plugin's root URL cannot be converted to a URI.
     * @throws IOException        If an I/O error occurs while listing the files and directories.
     */
    public static void foreachIn(@NotNull JavaPlugin plugin, @NotNull String root, @NotNull Consumer<Path> consumer) throws URISyntaxException, IOException {
        URL url = plugin.getClass().getResource("/" + root);
        if (null == url) return;

        URI uri = url.toURI();

        if (!"jar".equals(uri.getScheme())) {
            walk(Paths.get(uri), consumer);
            return;
        }

        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            walk(fileSystem.getPath("/" + root), consumer);
        } catch (FileSystemAlreadyExistsException e) {
            FileSystem fileSystem = FileSystems.getFileSystem(uri);
            walk(fileSystem.getPath("/" + root), consumer);
        }
    }

    private static void walk(@NotNull Path myPath, @NotNull Consumer<Path> consumer) throws IOException {
        Stream<Path> walk = Files.walk(myPath, 1);
        Iterator<Path> it = walk.iterator();
        while (it.hasNext()) consumer.accept(it.next());
        walk.close();
    }

    /**
     * Copies a resource file from the plugin's JAR file to the plugin's data folder.
     *
     * @param plugin The JavaPlugin instance of the plugin.
     * @param res    The resource file path within the JAR file to copy.
     * @throws IOException If an I/O error occurs during copying.
     */
    public static void copyResource(@NotNull JavaPlugin plugin, @NotNull String res) throws IOException {
        InputStream src = plugin.getClass().getResourceAsStream("/" + res);
        if (null == src) return;
        File file = new File(plugin.getDataFolder(), res);
        if (!file.exists()) file.mkdirs();
        else return;
        Files.copy(src, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
