package main.java;

import java.awt.Graphics;
import java.awt.Point;

import main.java.Mario.MarioState;

public class GameStateLevel1 extends GameState {

    private static boolean checkpointReached;

    public GameStateLevel1(){
        super();
        lvlId = 1;
        checkpointPosition = 84 * Block.SIZE; // 84
        initDefaultBehavior(checkpointReached);
    }


    public GameStateLevel1(MarioState marioState){
        this();
        mario.returnToState(marioState);
    }

    @Override
    protected void spawnMario() {
        if (checkpointReached) {
            mario = new Mario(new Point(checkpointPosition, Block.SIZE * 11));
        } else {
            mario = new Mario(new Point(Block.SIZE * 3, Block.SIZE * 11));
        }
    }

    @Override
    protected void loadImages() {
        getBackground("lvl1");
    }

    @Override
    protected void paintElements(Graphics g) {
        if(infoScreen){
            paintInfoScreen(g);
            return;
        }
        super.paintElements(g);
    }

    @Override
    protected void keyPressed(int k) {
        mario.keyPressed(k);
    }

    @Override
    protected void keyReleased(int k) {
        mario.keyReleased(k);
    }

    @Override
    protected void tick() {
        if(infoScreen){
            tickScoreScreen();
            return;
        }
        
        super.tick();
        
        if (!checkpointReached && mario.x >= checkpointPosition) {
            checkpointReached = true;
        }
    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        if(nextLevelRequest >= 2){
            Sound.stopAllSounds();
            GameState newGameState = new GameStateLevel2(mario.state);
            gameRunner.setCurrentGameState(newGameState);
            checkpointReached = false;
        }
    }

    public static void resetCheckpoint(){
        checkpointReached = false;
    }
    
}
