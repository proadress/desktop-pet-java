package main.pet;

import org.junit.jupiter.api.Test;

import java.awt.Dimension;
import java.awt.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MotionBoundsTest {
    private static final Dimension PET = new Dimension(100, 100);
    private static final Dimension SCREEN = new Dimension(800, 600);

    @Test
    void advancesInsideTheScreen() {
        MotionBounds.Step step = MotionBounds.advance(
                new Point(200, 200),
                PET,
                SCREEN,
                3,
                2
        );

        assertEquals(new Point(203, 202), step.location());
        assertEquals(3, step.horizontalSpeed());
        assertEquals(2, step.verticalSpeed());
    }

    @Test
    void reversesDirectionAtTheRightEdge() {
        MotionBounds.Step step = MotionBounds.advance(
                new Point(700, 200),
                PET,
                SCREEN,
                3,
                0
        );

        assertEquals(new Point(697, 200), step.location());
        assertEquals(-3, step.horizontalSpeed());
    }
}
