package main.pet;

import plugin.PetPlugin;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheetAnimation implements PetPlugin {
    private static final int FRAME_WIDTH = 100;
    private static final int FRAME_HEIGHT = 100;

    private final String name;
    private final String resourcePath;
    private final int rows;
    private final int columns;
    private final int ticksPerFrame;
    private List<ImageIcon> frames = List.of();
    private int tick;

    public SpriteSheetAnimation(
            String name,
            String resourcePath,
            int rows,
            int columns,
            int ticksPerFrame
    ) {
        this.name = name;
        this.resourcePath = resourcePath;
        this.rows = rows;
        this.columns = columns;
        this.ticksPerFrame = ticksPerFrame;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void build() {
        BufferedImage sheet = ResourceImages.read(resourcePath);
        int sourceWidth = sheet.getWidth() / columns;
        int sourceHeight = sheet.getHeight() / rows;
        List<ImageIcon> loadedFrames = new ArrayList<>(rows * columns);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                BufferedImage frame = sheet.getSubimage(
                        column * sourceWidth,
                        row * sourceHeight,
                        sourceWidth,
                        sourceHeight
                );
                Image scaled = frame.getScaledInstance(
                        FRAME_WIDTH,
                        FRAME_HEIGHT,
                        Image.SCALE_SMOOTH
                );
                loadedFrames.add(new ImageIcon(scaled));
            }
        }

        frames = List.copyOf(loadedFrames);
        tick = 0;
    }

    @Override
    public ImageIcon getImage() {
        if (frames.isEmpty()) {
            throw new IllegalStateException("Animation must be built before requesting a frame");
        }
        int frameIndex = tick / ticksPerFrame;
        tick = (tick + 1) % (frames.size() * ticksPerFrame);
        return frames.get(frameIndex);
    }

    public int frameCount() {
        return frames.size();
    }
}
