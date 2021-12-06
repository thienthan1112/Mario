package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.java.Enemies.Enemy;
import main.java.Mario.MarioState;

public class GameStateLevel4 extends GameState {

    private ArrayList<Block> bridge;
    private Block[] battleWall;
    private Block[] finishWall;
    private Rectangle finishWallCollider;
    private Enemy bowser;
    private Font textFont;

    private int bowserBattleStart;
    private int endSequenceCounter;
    private int bridgeCounter;
    private boolean battleStarted;
    private boolean battleFinished;
    private boolean canGoToMenu;

    public GameStateLevel4() {
        super();
        lvlId = 4;
        bowserBattleStart = 127 * Block.SIZE;
        textFont = TextFont.getFont().deriveFont(Font.BOLD, 30);
        initDefaultBehavior(false);
        createBattleWalls();
        findBridge();
        bowser = lvlMap.getBowser();
    }

    public GameStateLevel4(MarioState marioState) {
        this();
        mario.returnToState(marioState);
    }

    private void createBattleWalls(){
        Point blockPosition;

        battleWall = new Block[5];
        for(int i = 0; i<battleWall.length; i++){
            blockPosition = new Point(new Point(bowserBattleStart - Block.SIZE, Block.SIZE * (5 + i)));
            battleWall[i] = new Block(blockPosition, Block.CASTLE_GROUND);
        }

        finishWall = new Block[6];
        int xOffset = 0;
        int yOffset = -1;
        for(int i = 0; i<finishWall.length; i++){
            xOffset = (i%2==0) ? Block.SIZE : 0;
            yOffset = (xOffset==0) ? yOffset : yOffset + 1;
            blockPosition = new Point(new Point(bowserBattleStart + 18* Block.SIZE + xOffset, Block.SIZE * (6 + yOffset)));
            finishWall[i] = new Block(blockPosition, Block.CASTLE_GROUND);
        }

        finishWallCollider = new Rectangle(bowserBattleStart + 18* Block.SIZE, 6*Block.SIZE, Block.SIZE, 3 * Block.SIZE);
    }

    private void findBridge(){
        bridge = new ArrayList<>();
        for(Block[] array : lvlMap.getBlocks()){
            for(Block b : array){
                if(b.getId() == Block.CASTLE_BRIDGE){
                    bridge.add(b);
                }
            }
        }
    }

    @Override
    protected void spawnMario() {
        mario = new Mario(new Point(Block.SIZE * 2, Block.SIZE * 7));
    }

    @Override
    protected void loadImages() {
        getBackground("lvl4");
    }

    @Override
    protected void paintElements(Graphics g) {
        if (infoScreen) {
            paintInfoScreen(g);
            return;
        }
        // Can not use super because mario needs to be painted after the blocks
        paintBackground(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        lvlMap.paintBlocks(g, mario.x);
        mario.paintMario(g);
        lvlMap.paintFireballs(g);

        paintWalls(g);
        if(canGoToMenu){
            paintEndText(g);
        }
        
    }

    private void paintWalls(Graphics g){
        if (battleStarted) {
            for (Block b : battleWall) {
                b.paintBlock(g);
            }
        }

        if (!battleFinished) {
            for (Block b : finishWall) {
                b.paintBlock(g);
            }
        }
    }

    private void paintEndText(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(textFont);
        g.drawString("THANK YOU MARIO!", finishWallCollider.x + 12 * Block.SIZE, 5 * Block.SIZE);
        g.drawString("THIS IS THE END OF THE GAME", finishWallCollider.x + 9 * Block.SIZE, 6 * Block.SIZE);
        g.drawString("SEE YOU LATER!", finishWallCollider.x + 13 * Block.SIZE, 10 * Block.SIZE);
        g.setFont(textFont.deriveFont(Font.BOLD, 20));
        g.drawString("PRESS ENTER TO EXIT", finishWallCollider.x + 13 * Block.SIZE, 11 * Block.SIZE);
    }

    @Override
    protected void keyPressed(int k) {
        mario.keyPressed(k);
        if(true){
            if(k == KeyEvent.VK_ENTER){
                Sound.stopAllSounds();
                GameRunner.instance.restartGame();
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

        if(battleFinished){
            endSequence();
            return;
        }
        super.tick();
        if(!battleStarted){
            checkIfMarioIsInBattle();
        }
        if(!battleFinished){
            marioFinishWallCollision();
        }
    }

    private void checkIfMarioIsInBattle(){
        if(mario.x >= bowserBattleStart){
            for(Block b : battleWall){
                PhysicObject.addMapBlock(b);
            }
            battleStarted = true;
        }
    }

    private void marioFinishWallCollision(){
        if(finishWallCollider.intersects(mario)){
            mario.x -= (mario.x + mario.width) - finishWallCollider.x - 1;
        }
    }

    private void endSequence(){
        if(endSequenceCounter<100){
            hideBridge();
        }else if(endSequenceCounter==100){
            bowser.y += PhysicObject.getGravity();
            if(bowser.y > 15 * Block.SIZE){
                endSequenceCounter = 101;
            }
        }else if(endSequenceCounter == 101){
            mario.startWalkAnimation();
            endSequenceCounter = 102;
        }else if(mario.x < finishWallCollider.x + 15 * Block.SIZE){
            if(mario.x < finishWallCollider.x + 12 * Block.SIZE){
                GameRunner.instance.moveHorizontalScroll(mario.x);
            }
            mario.tick();
        }else{
            canGoToMenu = true;
            mario.showAsIdle();
            endSequenceCounter = 1000;
        }
    }

    private void hideBridge(){
        if(endSequenceCounter > 4){
            bridge.get(bridgeCounter--).hideBlock();
            endSequenceCounter = 0;
        }
        endSequenceCounter++;

        if(bridgeCounter < 0){
            endSequenceCounter = 100;
        }
    }

    @Override
    public void requestNextLevel() {
        Sound.stopAllSounds();
        Sound.makeSound(Sound.WORLD_CLEAR);
        battleFinished = true;
        endSequenceCounter = 0;
        bridgeCounter = bridge.size()-1;

    }
}