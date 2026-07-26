package main.mainProcess;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginPackagingTest {
    @Test
    void packagedServiceProvidersCanBeDiscovered() {
        Path pluginRoot = Path.of(System.getProperty("desktopPet.testPluginDir"));

        assertEquals(
                "Robot fox sprite plugin",
                new PluginManager(pluginRoot.resolve("pet")).getPetPlugins().get(0).getName()
        );
        assertEquals(
                "Plugin installer",
                new PluginManager(pluginRoot.resolve("tray")).getTrayPlugins().get(0).getName()
        );
    }
}
