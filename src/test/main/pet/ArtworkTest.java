package main.pet;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtworkTest {
    @Test
    void animationFramesAreTransparentSquareImages() {
        for (int frame = 1; frame <= 6; frame++) {
            BufferedImage image = ResourceImages.read("/picture/" + frame + ".png");
            assertEquals(256, image.getWidth());
            assertEquals(256, image.getHeight());
            assertTrue(image.getColorModel().hasAlpha());
        }
    }

    @Test
    void pluginSheetAndTrayIconsHaveExpectedDimensions() {
        BufferedImage sheet = ResourceImages.read("/picture/robot-fox-plugin.png");
        assertEquals(512, sheet.getWidth());
        assertEquals(256, sheet.getHeight());
        assertTrue(sheet.getColorModel().hasAlpha());

        for (String icon : new String[]{"run.png", "stop.png"}) {
            BufferedImage image = ResourceImages.read("/picture/" + icon);
            assertEquals(128, image.getWidth());
            assertEquals(128, image.getHeight());
            assertTrue(image.getColorModel().hasAlpha());
        }
    }
}
