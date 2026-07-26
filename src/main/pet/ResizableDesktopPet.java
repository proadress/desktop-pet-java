package main.pet;

import plugin.PetPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class ResizableDesktopPet extends JFrame {
    private static final long serialVersionUID = 1L;
    private static volatile ResizableDesktopPet instance;
    private final JLabel petLabel = new JLabel();
    private static final Dimension PET_SIZE = new Dimension(100, 100);
    private int dx;  // 水平移動速度
    private int dy;  // 垂直移動速度
    private transient PetPlugin petPlugin;
    private Timer timer;
    private static final int TIMER_DELAY_MILLISECONDS = 16;


    private ResizableDesktopPet() {
    }

    public static ResizableDesktopPet getInstance() {
        if (instance == null) {
            synchronized (ResizableDesktopPet.class) {
                if (instance == null) {
                    instance = new ResizableDesktopPet();
                }
            }
        }
        return instance;
    }

    // 建構函式，設定寵物的初始大小和速度
    public void build(PetPlugin petPlugin) {
        // 如果窗口已经显示，先停止计时器并销毁窗口
        if (isVisible()) {
            stopTimer();
            dispose();
        }

        this.petPlugin = petPlugin;

        // 设置窗口无边框和背景透明
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // 使用插件初始化窗口
        petPlugin.build();
        petLabel.setIcon(petPlugin.getImage());

        // 设置布局和添加组件
        setLayout(new BorderLayout());
        add(petLabel, BorderLayout.CENTER);

        // 调整窗口大小并居中显示
        pack();
        setLocationRelativeTo(null);

        // 显示窗口并启动计时器
        setVisible(true);
        startTimer();
    }

    // 启动计时器
    private void startTimer() {
        setVisible(true);
        timer = new Timer(TIMER_DELAY_MILLISECONDS, e -> {
            movePet();
            petLabel.setIcon(dx >= 0 ? petPlugin.getImage() : flipImageHorizontally(petPlugin.getImage()));
        });
        timer.start();
    }

    // 停止计时器
    public void stopTimer() {
        setVisible(false);
        if (timer != null) {
            timer.stop();
        }
    }

    public void resetTimer() {
        stopTimer();
        startTimer();
    }


    // 設定初始位置的方法
    public void setInitialPosition(int x, int y) {
        setLocation(x, y);
    }


    // 設定移動速度的方法
    public void setMoveSpeed(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }


    // 水平翻轉圖像方法
    private static ImageIcon flipImageHorizontally(ImageIcon icon) {
        Image originalImage = icon.getImage();
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, width, 0, 0, height, null);
        g2d.dispose();

        return new ImageIcon(flippedImage);
    }

    // 移動寵物方法
    private void movePet() {
        MotionBounds.Step step = MotionBounds.advance(
                getLocation(),
                PET_SIZE,
                Toolkit.getDefaultToolkit().getScreenSize(),
                dx,
                dy
        );
        dx = step.horizontalSpeed();
        dy = step.verticalSpeed();
        setLocation(step.location());
    }
}
