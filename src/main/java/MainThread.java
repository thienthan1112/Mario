package main.java;

public class MainThread extends Thread{
    
    public static final int FPS_TARGET = 60;

    public GameRunner gameRunner;

    private boolean isRunning;
    private long ticksPerSecond;
    private long nextTickTime;

    public MainThread(String name){
        super(name);
        ticksPerSecond = 1000/FPS_TARGET;
        nextTickTime = System.currentTimeMillis();
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        while (true) {
            if (System.currentTimeMillis() > nextTickTime && isRunning) {
                nextTickTime = System.currentTimeMillis() + ticksPerSecond;
                doThreadActions();
            }
        }
    }

    private void doThreadActions() {
        gameRunner.tick();
        gameRunner.repaint();
    }

    public void pauseThread() {
        isRunning = false;
    }

    public void resumeThread() {
        isRunning = true;
    }
}
