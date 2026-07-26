package main.mainProcess;

import plugin.PetPlugin;
import plugin.TrayPlugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public final class PluginManager {
    private final Path pluginDirectory;

    public PluginManager(Path pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    public List<TrayPlugin> getTrayPlugins() {
        return loadPlugins(TrayPlugin.class);
    }

    public List<PetPlugin> getPetPlugins() {
        return loadPlugins(PetPlugin.class);
    }

    private <T> List<T> loadPlugins(Class<T> pluginType) {
        if (Files.notExists(pluginDirectory)) {
            return List.of();
        }

        List<T> plugins = new ArrayList<>();
        try (Stream<Path> files = Files.list(pluginDirectory)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".jar"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> loadJar(path, pluginType, plugins));
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to scan plugin directory: " + pluginDirectory,
                    exception
            );
        }
        return List.copyOf(plugins);
    }

    private static <T> void loadJar(Path jar, Class<T> pluginType, List<T> plugins) {
        try {
            URL[] urls = {jar.toUri().toURL()};
            URLClassLoader loader = new URLClassLoader(urls, pluginType.getClassLoader());
            ServiceLoader.load(pluginType, loader).forEach(plugins::add);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load plugin JAR: " + jar, exception);
        }
    }
}
