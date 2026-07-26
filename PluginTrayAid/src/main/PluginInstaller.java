package plugins.tray;

import plugin.TrayPlugin;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.MenuItem;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class PluginInstaller implements TrayPlugin {
    private final Path petPluginDirectory = Path.of(
            System.getProperty("desktopPet.pluginDir", "build/plugins"),
            "pet"
    );

    @Override
    public String getName() {
        return "Plugin installer";
    }

    @Override
    public void build() {
        try {
            Files.createDirectories(petPluginDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to prepare plugin directory: " + petPluginDirectory,
                    exception
            );
        }
    }

    @Override
    public List<MenuItem> getMenuItems() {
        build();
        MenuItem install = new MenuItem("Install pet plugin");
        install.addActionListener(event -> chooseAndInstall());
        return List.of(install);
    }

    private void chooseAndInstall() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JAR files", "jar"));
        chooser.setDialogTitle("Choose a pet plugin JAR");

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path selected = chooser.getSelectedFile().toPath();
        Path destination = petPluginDirectory.resolve(selected.getFileName());
        try {
            Files.copy(selected, destination, StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(
                    null,
                    "Plugin installed. Restart the app to load it:\n" + destination.toAbsolutePath()
            );
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "Plugin installation failed: " + exception.getMessage(),
                    "Installation error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
