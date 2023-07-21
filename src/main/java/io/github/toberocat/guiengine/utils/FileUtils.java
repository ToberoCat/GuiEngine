package io.github.toberocat.guiengine.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created: 21.07.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class FileUtils {

    public static @NotNull File copyAll(@NotNull JavaPlugin plugin, @NotNull String root) throws IOException, URISyntaxException {
        list(plugin, root).forEach(x -> {
            if (x.toString().equals(root)) return;
            try {
                String file = root + "/" + x.getFileName();
                if (Files.isDirectory(x)) {
                    copyAll(plugin, file);
                } else {
                    copyResource(plugin, file);
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        return new File(plugin.getDataFolder(), root);
    }

    public static List<Path> list(@NotNull JavaPlugin plugin, @NotNull String root) throws URISyntaxException, IOException {
        java.net.URL url = plugin.getClass().getResource("/" + root);
        if (url == null) return Collections.emptyList();
        java.net.URI uri = url.toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                myPath = fileSystem.getPath("/" + root);
            } catch (FileSystemAlreadyExistsException e) {
                FileSystem fileSystem = FileSystems.getFileSystem(uri);
                myPath = fileSystem.getPath("/" + root);
            }
        } else {
            myPath = Paths.get(uri);
        }

        Stream<Path> walk = Files.walk(myPath, 1);
        List<Path> paths = new LinkedList<>();
        Iterator<Path> it = walk.iterator();
        while (it.hasNext()) paths.add(it.next());
        walk.close();
        return paths;
    }

    public static void copyResource(@NotNull JavaPlugin plugin, @NotNull String res) throws IOException {
        InputStream src = plugin.getClass().getResourceAsStream("/" + res);
        if (src == null) return;
        File file = new File(plugin.getDataFolder(), res);
        if (!file.exists()) file.mkdirs();
        else return;
        Files.copy(src, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
