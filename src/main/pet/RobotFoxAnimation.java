package main.pet;

import plugin.PetPlugin;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public final class RobotFoxAnimation implements PetPlugin {
    private static final int IMAGE_COUNT = 6;
    private static final int TICKS_PER_FRAME = 5;
    private List<ImageIcon> frames = List.of();
    private int tick;

    @Override
    public String getName() {
        return "Robot fox";
    }

    @Override
    public void build() {
        List<ImageIcon> loadedFrames = new ArrayList<>(IMAGE_COUNT);
        for (int index = 1; index <= IMAGE_COUNT; index++) {
            loadedFrames.add(ResourceImages.scaled("/picture/" + index + ".png", 100, 100));
        }
        frames = List.copyOf(loadedFrames);
        tick = 0;
    }

    @Override
    public ImageIcon getImage() {
        if (frames.isEmpty()) {
            throw new IllegalStateException("Animation must be built before requesting a frame");
        }
        int frameIndex = tick / TICKS_PER_FRAME;
        tick = (tick + 1) % (frames.size() * TICKS_PER_FRAME);
        return frames.get(frameIndex);
    }
}
