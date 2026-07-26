package main.pet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpriteSheetAnimationTest {
    @Test
    void requiresBuildBeforeReadingAFrame() {
        SpriteSheetAnimation animation = new SpriteSheetAnimation(
                "Test",
                "/picture/robot-fox-plugin.png",
                2,
                4,
                5
        );

        assertThrows(IllegalStateException.class, animation::getImage);
    }

    @Test
    void slicesAllFramesFromTheSpriteSheet() {
        SpriteSheetAnimation animation = new SpriteSheetAnimation(
                "Test",
                "/picture/robot-fox-plugin.png",
                2,
                4,
                5
        );

        animation.build();

        assertEquals(8, animation.frameCount());
        assertNotNull(animation.getImage());
    }
}
