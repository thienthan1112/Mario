package main.java;

import javax.swing.*;

import java.awt.*;

public class WindowManager extends JFrame {
    private static final long serialVersionUID = 001L;

    public static final int WINDOW_HEIGHT = 800;
    public static final int MAX_WIDTH = 1540;
    public static final String APP_NAME = "Super Mario by Nhóm 3";
    public static int windowWidth;

    public WindowManager() {
        super(APP_NAME);
        findWindowSize();
        startGameRunner();
        setJFrameRules();
    }

    private void findWindowSize() {
        Dimension screenDimension = findCurrentScreenDimension();
        windowWidth = (int) screenDimension.getWidth();
        if(windowWidth>=MAX_WIDTH){
            windowWidth = MAX_WIDTH;
        }
    }

    private void startGameRunner() {
        GameRunner gameRunner = GameRunner.instance;
        gameRunner.initializeGameRunner(this);
        add(gameRunner);
    }

    private void setJFrameRules() {
        setSize(windowWidth, WINDOW_HEIGHT);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Dimension findCurrentScreenDimension() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }
}