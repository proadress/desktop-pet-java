package main.tray;

import main.pet.ResizableDesktopPet;
import main.pet.ResourceImages;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.List;

public final class Tray {
    private static volatile Tray instance;
    private TrayIcon trayIcon;

    private Tray() {
    }

    public static Tray getInstance() {
        if (instance == null) {
            synchronized (Tray.class) {
                if (instance == null) {
                    instance = new Tray();
                }
            }
        }
        return instance;
    }

    public void start(List<MenuItem> menuItems) {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported on this desktop");
            return;
        }

        trayIcon = new TrayIcon(
                ResourceImages.read("/picture/run.png"),
                "Desktop Pet",
                createPopupMenu(menuItems)
        );
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(event -> togglePet());

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException exception) {
            throw new IllegalStateException("Unable to add the desktop pet to the system tray", exception);
        }
    }

    private PopupMenu createPopupMenu(List<MenuItem> menuItems) {
        PopupMenu popup = new PopupMenu();

        MenuItem toggle = new MenuItem("Start or stop pet");
        toggle.addActionListener(event -> togglePet());
        popup.add(toggle);

        if (!menuItems.isEmpty()) {
            popup.addSeparator();
            menuItems.forEach(popup::add);
        }

        popup.addSeparator();
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(event -> System.exit(0));
        popup.add(exit);
        return popup;
    }

    private void togglePet() {
        ResizableDesktopPet pet = ResizableDesktopPet.getInstance();
        if (pet.isVisible()) {
            updateTrayIcon("/picture/stop.png");
            pet.stopTimer();
        } else {
            updateTrayIcon("/picture/run.png");
            pet.resetTimer();
        }
    }

    private void updateTrayIcon(String resourcePath) {
        if (trayIcon != null) {
            trayIcon.setImage(ResourceImages.read(resourcePath));
        }
    }
}
