package main.pet;

import java.awt.Dimension;
import java.awt.Point;

public final class MotionBounds {
    private MotionBounds() {
    }

    public static Step advance(
            Point current,
            Dimension window,
            Dimension screen,
            int horizontalSpeed,
            int verticalSpeed
    ) {
        int nextHorizontalSpeed = horizontalSpeed;
        int nextVerticalSpeed = verticalSpeed;
        int nextX = current.x + nextHorizontalSpeed;
        int nextY = current.y + nextVerticalSpeed;

        if (nextX < 0 || nextX + window.width > screen.width) {
            nextHorizontalSpeed = -nextHorizontalSpeed;
            nextX = current.x + nextHorizontalSpeed;
        }
        if (nextY < 0 || nextY + window.height > screen.height) {
            nextVerticalSpeed = -nextVerticalSpeed;
            nextY = current.y + nextVerticalSpeed;
        }

        return new Step(new Point(nextX, nextY), nextHorizontalSpeed, nextVerticalSpeed);
    }

    public record Step(Point location, int horizontalSpeed, int verticalSpeed) {
    }
}
