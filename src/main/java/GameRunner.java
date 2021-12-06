package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GameRunner extends JPanel implements KeyListener {

    private static final long serialVersionUID = 001L;

    public static final GameRunner instance = new GameRunner();
    private Font textFont;
    private WindowManager window;
    private MainThread mainThread;
    private GameState currentGameState;
    private Score scoreManager;

    public int cameraOffset;
    private int cameraX;

    private GameRunner() {
        super();
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);
    }

    public void initializeGameRunner(WindowManager wManager) {
        currentGameState = new GameStateMenu();
        attachRunnerToMainThread();
        scoreManager = new Score();
        window = wManager;
        cameraOffset = WindowManager.windowWidth / 3;
    }

    private void attachRunnerToMainThread() {
        mainThread = new MainThread("MarioThread");
        mainThread.gameRunner = this;
    }

    public void definePanelSize(Dimension dimension) {
        setPreferredSize(dimension);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(-cameraX, 0); 
        g.setClip(cameraX, getY(), window.getWidth(), window.getHeight());     
        currentGameState.paintElements(g);
        scoreManager.paint(g, cameraX);  
    }

    @Override
    public void keyTyped(KeyEvent e) {
//    	int m = 1;
//    	 if (e.getKeyCode() == KeyEvent.VK_M) {
//    		 m = m+1;
//         }
//    	 if(m%2==0) {
//    		 Sound.stopAllSounds();
//    	 }
    	 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentGameState.keyPressed(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_W) {
            mainThread.resumeThread();
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            mainThread.pauseThread();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        currentGameState.keyReleased(e.getKeyCode());
    }

    public void tick(){
        currentGameState.tick();
    }

    public void setCurrentGameState(GameState gameState){
        currentGameState = gameState;
    }

    public void restartCurrentLevel(){
        switch(currentGameState.lvlId){
            case 1:
                currentGameState = new GameStateLevel1();
                break;
            case 2:
                currentGameState = new GameStateLevel2();
                break;
            case 3:
                currentGameState = new GameStateLevel3();
                break;
            case 4:
                currentGameState = new GameStateLevel4();
                break;
        }
        Sound.stopAllSounds();
        System.gc();
    }


    public void stopTimer(){
        currentGameState.stopTimer();
    }

    public void restartGame(){
        cameraX = 0;
        Sound.stopAllSounds();
        moveAbsoluteHorizontalScroll(0);
        currentGameState = new GameStateMenu();
        scoreManager = new Score();
    }

    public void endCurrentLevel(){
        currentGameState.endLevel();
    }

    public void requestNextLevel(){
        currentGameState.requestNextLevel();
    }

    public void moveHorizontalScroll(int newPosition){
        if (newPosition > cameraOffset) {
            cameraX = newPosition - cameraOffset;
        }
    }

    public void moveAbsoluteHorizontalScroll(int newPosition){
        cameraX = newPosition;
    }
}
