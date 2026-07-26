package main.mainProcess;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginDirectoriesTest {
    @Test
    void honorsAnExplicitPluginDirectory() {
        Path pluginDirectory = PluginDirectories.resolve(
                "/srv/desktop-pet/plugins",
                Path.of("/opt/desktop-pet/lib/desktop-pet.jar"),
                Path.of("/workspace")
        );

        assertEquals(Path.of("/srv/desktop-pet/plugins"), pluginDirectory);
    }

    @Test
    void findsPluginsBesideAnInstalledDistribution() {
        Path pluginDirectory = PluginDirectories.resolve(
                null,
                Path.of("/opt/desktop-pet/lib/desktop-pet.jar"),
                Path.of("/workspace")
        );

        assertEquals(Path.of("/opt/desktop-pet/plugins"), pluginDirectory);
    }

    @Test
    void fallsBackToTheGradleBuildDirectoryDuringDevelopment() {
        Path pluginDirectory = PluginDirectories.resolve(
                null,
                Path.of("/workspace/build/classes/java/main"),
                Path.of("/workspace")
        );

        assertEquals(Path.of("/workspace/build/plugins"), pluginDirectory);
    }
}
