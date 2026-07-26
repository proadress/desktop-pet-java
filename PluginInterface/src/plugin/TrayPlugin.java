package plugin;

import java.awt.*;
import java.util.List;

public interface TrayPlugin {
    String getName();

    void build();

    List<MenuItem> getMenuItems();
}