package main.mainProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public final class FileData {
    private final Path settingsFile;
    private final Properties settings = new Properties();

    public FileData(Path settingsFile) {
        this.settingsFile = settingsFile;
    }

    public static FileData defaultStore() {
        Path appDirectory = Path.of(System.getProperty("user.home"), ".desktop-pet");
        return new FileData(appDirectory.resolve("settings.properties"));
    }

    public void load() throws IOException {
        settings.clear();
        if (Files.notExists(settingsFile)) {
            save();
            return;
        }

        try (InputStream input = Files.newInputStream(settingsFile)) {
            settings.load(input);
        }
    }

    public void set(String key, String value) throws IOException {
        settings.setProperty(key, value);
        save();
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(settings.getProperty(key));
    }

    public void delete(String key) throws IOException {
        settings.remove(key);
        save();
    }

    public Path path() {
        return settingsFile;
    }

    private void save() throws IOException {
        Path parent = settingsFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (OutputStream output = Files.newOutputStream(settingsFile)) {
            settings.store(output, "Desktop Pet Settings");
        }
    }
}
