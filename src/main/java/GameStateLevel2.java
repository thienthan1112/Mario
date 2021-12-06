package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import main.java.Mario.MarioState;

public class GameStateLevel2 extends GameState{

    private static boolean checkpointReached;

    private Rectangle tpPosition;
    private boolean marioCanTp;
    private boolean marioHasTp;
    
    public GameStateLevel2(){
        super();
        lvlId = 2;
        checkpointPosition = 88 * Block.SIZE;// 88
        initDefaultBehavior(checkpointReached);
        lvlMap.turnOnDarkMode();
        tpPosition = new Rectangle(167 * Block.SIZE - Block.SIZE / 10, Block.SIZE * 6, Block.SIZE, Block.SIZE * 2);
    }

    public GameStateLevel2(MarioState marioState){
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
        getBackground("lvl2");
    }

    @Override
    protected void paintElements(Graphics g) {
        if (infoScreen) {
            paintInfoScreen(g);
            return;
        }
        super.paintElements(g);
        g.setColor(Color.RED);
    }

    @Override
    protected void keyPressed(int k) {
        mario.keyPressed(k);
        if(!marioHasTp && marioCanTp){
            if(k == KeyEvent.VK_RIGHT){
                timerEnabled = false;
                mario.startWalkAnimationNoCollisions();
                Sound.stopAllSounds();
                Sound.makeSound(Sound.PIPE);
            }
        }
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

        if(!marioHasTp){
            marioCanTp = (mario.isFalling) ? false : tpPosition.intersects(mario);
            if (marioCanTp && mario.x > tpPosition.x) {
                marioHasTp = true;
                timerEnabled = true;
                mario.setLocation(mario.x + Block.SIZE * 29 - mario.width/2, Block.SIZE * 10);
                mario.exitPipe(Block.SIZE * 10);
                lvlMap.turnOffDarkMode();
                Sound.loopSound(Sound.LVL1);
            }
        }

    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        if (nextLevelRequest >= 2) {
            Sound.stopAllSounds();
            GameState newGameState = new GameStateLevel3(mario.state);
            gameRunner.setCurrentGameState(newGameState);
            checkpointReached = false;
        }
    }

    public static void resetCheckpoint() {
        checkpointReached = false;
    }
    
}
