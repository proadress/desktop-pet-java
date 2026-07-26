package main.mainProcess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginManagerTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void returnsAnEmptyListWhenNoPluginJarsExist() {
        PluginManager manager = new PluginManager(temporaryDirectory.resolve("missing"));

        assertTrue(manager.getPetPlugins().isEmpty());
        assertTrue(manager.getTrayPlugins().isEmpty());
    }
}
