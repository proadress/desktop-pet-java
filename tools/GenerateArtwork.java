import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GenerateArtwork {
    private static final int[][] FRAME_MOTION = {
            {8, -3, 102, 96},
            {3, -1, 100, 100},
            {-3, 2, 98, 104},
            {-5, 3, 100, 102},
            {1, 1, 102, 98},
            {7, -2, 101, 97},
            {2, 0, 99, 101},
            {-4, 2, 98, 103}
    };

    private GenerateArtwork() {
    }

    public static void main(String[] args) throws IOException {
        Path root = args.length == 0 ? Path.of(".") : Path.of(args[0]);
        Path sourcePath = root.resolve("artwork/robot-fox-source.png");
        Path outputDirectory = root.resolve("picture");
        Files.createDirectories(outputDirectory);

        BufferedImage source = ImageIO.read(sourcePath.toFile());
        if (source == null) {
            throw new IOException("Unable to read " + sourcePath);
        }

        BufferedImage mascot = cropTransparentBorder(source);
        for (int frame = 0; frame < 6; frame++) {
            ImageIO.write(
                    renderFrame(mascot, 256, 256, FRAME_MOTION[frame]),
                    "png",
                    outputDirectory.resolve((frame + 1) + ".png").toFile()
            );
        }

        BufferedImage sheet = new BufferedImage(512, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sheetGraphics = sheet.createGraphics();
        applyQualityHints(sheetGraphics);
        for (int frame = 0; frame < FRAME_MOTION.length; frame++) {
            BufferedImage image = renderFrame(mascot, 128, 128, FRAME_MOTION[frame]);
            sheetGraphics.drawImage(image, (frame % 4) * 128, (frame / 4) * 128, null);
        }
        sheetGraphics.dispose();
        ImageIO.write(sheet, "png", outputDirectory.resolve("robot-fox-plugin.png").toFile());

        ImageIO.write(drawTrayIcon(true), "png", outputDirectory.resolve("run.png").toFile());
        ImageIO.write(drawTrayIcon(false), "png", outputDirectory.resolve("stop.png").toFile());
    }

    private static BufferedImage cropTransparentBorder(BufferedImage source) {
        int minX = source.getWidth();
        int minY = source.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                if ((source.getRGB(x, y) >>> 24) != 0) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            throw new IllegalArgumentException("Source artwork is fully transparent");
        }
        return source.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    private static BufferedImage renderFrame(
            BufferedImage source,
            int width,
            int height,
            int[] motion
    ) {
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = frame.createGraphics();
        applyQualityHints(graphics);

        double baseScale = Math.min(
                width * 0.88 / source.getWidth(),
                height * 0.78 / source.getHeight()
        );
        double scaleX = baseScale * motion[2] / 100.0;
        double scaleY = baseScale * motion[3] / 100.0;
        double verticalOffset = height * motion[0] / 100.0;
        double rotation = Math.toRadians(motion[1]);

        AffineTransform transform = new AffineTransform();
        transform.translate(width / 2.0, height / 2.0 + verticalOffset);
        transform.rotate(rotation);
        transform.scale(scaleX, scaleY);
        transform.translate(-source.getWidth() / 2.0, -source.getHeight() / 2.0);
        graphics.drawRenderedImage(source, transform);
        graphics.dispose();
        return frame;
    }

    private static BufferedImage drawTrayIcon(boolean running) {
        BufferedImage icon = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = icon.createGraphics();
        applyQualityHints(graphics);
        graphics.setComposite(AlphaComposite.SrcOver);

        Color navy = new Color(16, 39, 78);
        Color coral = new Color(244, 103, 72);
        Color cream = new Color(255, 239, 202);
        Color cyan = new Color(52, 211, 235);

        graphics.setColor(navy);
        graphics.fill(new RoundRectangle2D.Double(8, 8, 112, 112, 32, 32));
        graphics.setColor(running ? cyan : coral);
        graphics.fillOval(24, 24, 80, 80);
        graphics.setColor(cream);
        if (running) {
            int[] xPoints = {51, 51, 86};
            int[] yPoints = {40, 88, 64};
            graphics.fillPolygon(xPoints, yPoints, 3);
        } else {
            graphics.fill(new RoundRectangle2D.Double(45, 45, 38, 38, 8, 8));
        }
        graphics.dispose();
        return icon;
    }

    private static void applyQualityHints(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
