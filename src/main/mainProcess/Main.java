package main.mainProcess;

import main.pet.RobotFoxAnimation;
import main.pet.ResizableDesktopPet;
import main.tray.Tray;
import plugin.PetPlugin;
import plugin.TrayPlugin;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.MenuItem;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        loadSettings();

        Path pluginRoot = Path.of(System.getProperty("desktopPet.pluginDir", "build/plugins"));
        List<PetPlugin> petPlugins = new PluginManager(pluginRoot.resolve("pet")).getPetPlugins();
        List<TrayPlugin> trayPlugins = new PluginManager(pluginRoot.resolve("tray")).getTrayPlugins();

        List<MenuItem> menuItems = new ArrayList<>();
        if (!petPlugins.isEmpty()) {
            menuItems.add(createPetSelector(petPlugins));
        }
        trayPlugins.stream()
                .flatMap(plugin -> plugin.getMenuItems().stream())
                .forEach(menuItems::add);

        Tray.getInstance().start(menuItems);
        ResizableDesktopPet pet = ResizableDesktopPet.getInstance();
        pet.build(new RobotFoxAnimation());
        pet.setMoveSpeed(1, 0);
    }

    private static MenuItem createPetSelector(List<PetPlugin> plugins) {
        MenuItem item = new MenuItem("Choose pet");
        item.addActionListener(event -> {
            JComboBox<String> choices = new JComboBox<>(
                    plugins.stream().map(PetPlugin::getName).toArray(String[]::new)
            );
            int result = JOptionPane.showConfirmDialog(
                    null,
                    choices,
                    "Choose a pet",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String selectedName = (String) choices.getSelectedItem();
            plugins.stream()
                    .filter(plugin -> plugin.getName().equals(selectedName))
                    .findFirst()
                    .ifPresent(plugin -> {
                        ResizableDesktopPet pet = ResizableDesktopPet.getInstance();
                        pet.build(plugin);
                        pet.setMoveSpeed(1, 0);
                    });
        });
        return item;
    }

    private static void loadSettings() {
        FileData settings = FileData.defaultStore();
        try {
            settings.load();
        } catch (IOException exception) {
            System.err.println("Unable to load settings from " + settings.path() + ": "
                    + exception.getMessage());
        }
    }
}
