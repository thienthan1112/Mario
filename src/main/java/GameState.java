package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameState {

    private static final int BACKGROUND_OFFSET = -31;
    protected static final int YPOSITION_KILL_LIMIT = WindowManager.WINDOW_HEIGHT + Block.SIZE;
    protected static Score scoreManager;
    private static int info_screen_ticks;

    protected BufferedImage background;
    protected GameRunner gameRunner;
    protected LevelMap lvlMap;
    protected Mario mario;
    protected int nextLevelRequest;
    protected int lvlId;
    protected int checkpointPosition;
    protected boolean imagesLoaded;
    protected boolean infoScreen;
    protected boolean levelFinished;
    protected boolean timerEnabled;

    private int infoScreenCounter;
    
    public GameState() {
        loadImages();
        scoreManager = Score.getInstance();
        gameRunner = GameRunner.instance;
    }

    public void initDefaultBehavior(boolean checkpointReached){
        info_screen_ticks = 100;
        timerEnabled = true;
        infoScreen = true;
        nextLevelRequest = 0;
        scoreManager.stopAddingTimeToPoints();
        scoreManager.setTimer(360);
        scoreManager.setLevel(lvlId);
        setCameraPosition(checkpointReached);
        spawnMario();
        createLevelMap(lvlId);
        definePanelSize(SpriteAssets.getBackground("lvl" + lvlId).getWidth(), WindowManager.WINDOW_HEIGHT);

    }

    protected abstract void spawnMario();

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    public abstract void requestNextLevel();

    protected void tick(){
        mario.tick();

        lvlMap.tickInteractableBlocks();
        lvlMap.tickPickUps();
        lvlMap.tickEnemies();
        lvlMap.tickFireballs();
        lvlMap.removeUsedObjects();
        lvlMap.addNewObjects();

        if (mario.y >= YPOSITION_KILL_LIMIT) {
            mario.killMario();
        }
        if(timerEnabled){
            scoreManager.tick();
            if (scoreManager.getTimer() <= 0 && !levelFinished) {
                System.out.println("kill");
                mario.killMario();
            }
        }        
    }

    protected void tickScoreScreen() {
        if (infoScreenCounter == 1 && scoreManager.getLives() <= 0){
            Sound.makeSound(Sound.GAME_OVER);
            info_screen_ticks *=3;
        }
            
        if (infoScreenCounter > info_screen_ticks) {
            playLevelSong();
            infoScreen = false;

            if (scoreManager.getLives() <= 0) {
                GameRunner.instance.restartGame();
            }
        }
        infoScreenCounter++;
    }

    

    protected void setCameraPosition(boolean checkpointReached){
        if (checkpointReached) {
            GameRunner.instance.moveHorizontalScroll(checkpointPosition);
        } else {
            GameRunner.instance.moveAbsoluteHorizontalScroll(0);
        }
    }

    protected void getBackground(String backgroundName) {
        background = SpriteAssets.getBackground(backgroundName);
    }

    protected void paintElements(Graphics g){
        paintBackground(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        mario.paintMario(g);
        lvlMap.paintBlocks(g, mario.x);
        lvlMap.paintFireballs(g);
    }

    protected void paintBackground(Graphics g){
        g.drawImage(background, 0, BACKGROUND_OFFSET, gameRunner);
    }

    protected void paintInfoScreen(Graphics g){
        scoreManager.paintFullDetails(g, lvlId);
    }

    protected void definePanelSize(int width, int height){
        Dimension dimension = new Dimension(width, height);
        gameRunner.definePanelSize(dimension);
    }

    protected void createLevelMap(int id){
        if(this instanceof GameStateMenu){
            MyLogger.logInfoMessage("Creating a level map for the menu is unsupported. Aborting task.");
            return;
        }
        lvlMap = new LevelMap(id);
    }

    protected void displayScoreScreen(){
        infoScreen = true;
        infoScreenCounter = 0;
    }

    protected void playLevelSong(){
        switch(lvlId){
            case 1:
                Sound.loopSound(Sound.LVL1);
                break;
            case 2:
                Sound.loopSound(Sound.LVL2);
                break;
            case 3:
                Sound.loopSound(Sound.LVL3);
                break;
            case 4:
                Sound.loopSound(Sound.LVL4);
                break;
        }
        
    }

    protected void endLevel(){
        if(levelFinished){
            return;
        }
        
        if (this instanceof GameStateMenu) {
            MyLogger.logInfoMessage("Ending the menu is not allowed. Aborting task.");
            return;
        }

        Sound.stopAllSounds();
        if(lvlId != 4){
            Sound.makeSound(Sound.LEVEL_CLEAR);
        }else{
            Sound.makeSound(Sound.WORLD_CLEAR);
        }
        
        mario.startEndAnimation(lvlMap.getFlag());
        scoreManager.addTimeToPoints();
        levelFinished = true;
    }


    public void stopTimer(){
        timerEnabled = false;
    }

    
}
