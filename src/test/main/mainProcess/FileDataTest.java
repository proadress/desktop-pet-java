package main.mainProcess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDataTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void persistsAndDeletesSettings() throws IOException {
        Path settingsPath = temporaryDirectory.resolve("desktop-pet/settings.properties");
        FileData settings = new FileData(settingsPath);
        settings.load();
        settings.set("speed", "2");

        FileData reloaded = new FileData(settingsPath);
        reloaded.load();
        assertEquals("2", reloaded.get("speed").orElseThrow());

        reloaded.delete("speed");
        assertTrue(reloaded.get("speed").isEmpty());
    }
}
