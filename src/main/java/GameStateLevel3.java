package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import main.java.Mario.MarioState;

public class GameStateLevel3 extends GameState {

    private static boolean checkpointReached;
    private ArrayList<Block> grassSupportBlocks;

    public GameStateLevel3() {
        super();
        lvlId = 3;
        checkpointPosition = 60 * Block.SIZE; // 60 84
        initDefaultBehavior(checkpointReached);
        findGrassSupportBlocks();
    }

    public GameStateLevel3(MarioState marioState) {
        this();
        mario.returnToState(marioState);
    }

    private void findGrassSupportBlocks(){
        grassSupportBlocks = new ArrayList<>();
        for (Block[] arrayB : lvlMap.getBlocks()) {
            for (Block b : arrayB) {
                if(b.getId()== Block.GRASS_SUPPORT){
                    grassSupportBlocks.add(b);
                }
            }
        }
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
        getBackground("lvl3");
    }

    @Override
    protected void paintElements(Graphics g) {
        if (infoScreen) {
            paintInfoScreen(g);
            return;
        }
        //Can not use super due to grass support blocks order
        paintBackground(g);
        paintGrassSupportBlocks(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        mario.paintMario(g);
        lvlMap.paintBlocks(g, mario.x);
        lvlMap.paintFireballs(g);
    }

    private void paintGrassSupportBlocks(Graphics g){
        for (Block b : grassSupportBlocks) {
            b.paintBlock(g);
        }
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
        if (infoScreen) {
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
            GameState newGameState = new GameStateLevel4(mario.state);
            gameRunner.setCurrentGameState(newGameState);
            checkpointReached = false;
        }
    }

    public static void resetCheckpoint() {
        checkpointReached = false;
    }
}