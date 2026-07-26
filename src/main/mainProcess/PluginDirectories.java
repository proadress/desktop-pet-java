package main.mainProcess;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

final class PluginDirectories {
    private static final String OVERRIDE_PROPERTY = "desktopPet.pluginDir";

    private PluginDirectories() {
    }

    static Path resolve() {
        return resolve(
                System.getProperty(OVERRIDE_PROPERTY),
                codeLocation(),
                Path.of("").toAbsolutePath()
        );
    }

    static Path resolve(String override, Path codeLocation, Path workingDirectory) {
        if (override != null && !override.isBlank()) {
            return Path.of(override);
        }

        Path locationDirectory = codeLocation;
        Path fileName = codeLocation.getFileName();
        if (fileName != null && fileName.toString().endsWith(".jar")) {
            locationDirectory = codeLocation.getParent();
        }

        if (locationDirectory != null
                && locationDirectory.getFileName() != null
                && locationDirectory.getFileName().toString().equals("lib")) {
            return locationDirectory.getParent().resolve("plugins");
        }

        return workingDirectory.resolve("build/plugins");
    }

    private static Path codeLocation() {
        URL location = Main.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            return Path.of(location.toURI());
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Unable to resolve application location", exception);
        }
    }
}
