package main.java;

import static main.java.Animator.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public class SpriteAssets {

    private static final int BACKGROUND_MENU = 0, 
                             BACKGROUND_LVL1 = 1, 
                             BACKGROUND_LVL2 = 2, 
                             BACKGROUND_LVL3 = 3,
                             BACKGROUND_LVL4 = 4;

    public static final int PREVIOUS_WINDOW_HEIGHT = 896;

    private static BufferedImage gameLogo;
    private static BufferedImage miniCoin;
    private static BufferedImage[] backgrounds;
    private static BufferedImage[] blockSprites;
    private static BufferedImage[] marioSprites;
    private static BufferedImage[] pickUps;
    private static BufferedImage[] enemies;
    private static BufferedImage[] fireball;

    static {
        try {
            loadGameLogo();
            loadMiniCoin();
            loadBackgrounds();
            loadBlockSprites();
            loadMarioSprites();
            loadPickUpSprites();
            loadEnemiesSprites();
            loadFireballSprites();
            System.gc();
        } catch (IOException e) {
            MyLogger.logErrorMessage("The sprites of the game could not be loaded.", e);
        }
    }

    private static void loadGameLogo() throws IOException {
        File logoFile = new File("res/backgrounds/logo.png");
        gameLogo = ImageIO.read(logoFile);
    }

    private static void loadMiniCoin() throws IOException{
        File miniCoinFile = new File("res/objects/oCoinMini.png");
        miniCoin = ImageIO.read(miniCoinFile);
    }

    private static void loadBackgrounds() throws IOException {
        backgrounds = new BufferedImage[5];
        backgrounds[BACKGROUND_MENU] = ImageIO.read(new File("res/backgrounds/menu.png"));
        backgrounds[BACKGROUND_LVL1] = ImageIO.read(new File("res/backgrounds/lvl1.png"));
        backgrounds[BACKGROUND_LVL2] = ImageIO.read(new File("res/backgrounds/lvl2.png"));
        backgrounds[BACKGROUND_LVL3] = ImageIO.read(new File("res/backgrounds/lvl3.png"));
        backgrounds[BACKGROUND_LVL4] = ImageIO.read(new File("res/backgrounds/lvl4.png"));

        rescaleSprites(backgrounds);
    }

    private static void loadBlockSprites() throws IOException {
        blockSprites = new BufferedImage[Block.BLOCK_COUNT];
        blockSprites[Block.EMPTY] = null;
        blockSprites[Block.GROUND] = ImageIO.read(new File("res/blocks/bGround.png"));
        blockSprites[Block.BREAKABLE] = ImageIO.read(new File("res/blocks/bBreakable.png"));
        blockSprites[Block.MISTERY] = ImageIO.read(new File("res/blocks/bMistery.png"));
        blockSprites[Block.SOLID] = ImageIO.read(new File("res/blocks/bSolid.png"));
        blockSprites[Block.PIPE_B_LEFT] = ImageIO.read(new File("res/blocks/bPipeBottomLeft.png"));
        blockSprites[Block.PIPE_B_RIGHT] = ImageIO.read(new File("res/blocks/bPipeBottomRight.png"));
        blockSprites[Block.PIPE_T_LEFT] = ImageIO.read(new File("res/blocks/bPipeTopLeft.png"));
        blockSprites[Block.PIPE_T_RIGHT] = ImageIO.read(new File("res/blocks/bPipeTopRight.png"));
        blockSprites[Block.USED] = ImageIO.read(new File("res/blocks/bUsed.png"));
        blockSprites[Block.FLAG_POST] = ImageIO.read(new File("res/blocks/bFlagPost.png"));
        blockSprites[Block.FLAG_TOP] = ImageIO.read(new File("res/blocks/bFlagTop.png"));

        blockSprites[Block.BREAKABLE_ANIM1] = ImageIO.read(new File("res/blocks/bBroken1.png"));
        blockSprites[Block.BREAKABLE_ANIM2] = ImageIO.read(new File("res/blocks/bBroken2.png"));
        blockSprites[Block.BREAKABLE_ANIM3] = ImageIO.read(new File("res/blocks/bBroken3.png"));

        blockSprites[Block.BREAKABLE_DARK] = ImageIO.read(new File("res/blocks/bBreakableDark.png"));
        blockSprites[Block.GROUND_DARK] = ImageIO.read(new File("res/blocks/bGroundDark.png"));
        blockSprites[Block.SOLID_DARK] = ImageIO.read(new File("res/blocks/bSolidDark.png"));

        blockSprites[Block.ENEMY_AI] = blockSprites[Block.EMPTY];

        blockSprites[Block.PIPEH_B_LEFT] = ImageIO.read(new File("res/blocks/bPipeHBottomLeft.png"));
        blockSprites[Block.PIPEH_B_RIGHT] = ImageIO.read(new File("res/blocks/bPipeHBottomRight.png"));
        blockSprites[Block.PIPEH_T_LEFT] = ImageIO.read(new File("res/blocks/bPipeHTopLeft.png"));
        blockSprites[Block.PIPEH_T_RIGHT] = ImageIO.read(new File("res/blocks/bPipeHTopRight.png"));

        blockSprites[Block.GRASS_LEFT] = ImageIO.read(new File("res/blocks/bGrassLeft.png"));
        blockSprites[Block.GRASS_CENTER] = ImageIO.read(new File("res/blocks/bGrassMiddle.png"));
        blockSprites[Block.GRASS_RIGHT] = ImageIO.read(new File("res/blocks/bGrassRight.png"));
        blockSprites[Block.GRASS_SUPPORT] = ImageIO.read(new File("res/blocks/bGrassSupport.png"));

        blockSprites[Block.CASTLE_GROUND] = ImageIO.read(new File("res/blocks/bGroundCastle.png"));
        blockSprites[Block.CASTLE_BRIDGE] = ImageIO.read(new File("res/blocks/bBridge.png"));

        rescaleSprites(blockSprites, Block.SIZE, Block.SIZE);
    }

    private static void loadMarioSprites() throws IOException{
        marioSprites = new BufferedImage[MARIO_SPRITE_COUNT];
        marioSprites[M_DEAD-M_START] = ImageIO.read(new File("res/mario/mDead.png"));
        marioSprites[M_SMALL_RIGHT_IDLE-M_START] = ImageIO.read(new File("res/mario/mSmallRightIdle.png"));
        marioSprites[M_SMALL_RIGHT_WALK1-M_START] = ImageIO.read(new File("res/mario/mSmallRightWalk1.png"));
        marioSprites[M_SMALL_RIGHT_WALK2-M_START] = ImageIO.read(new File("res/mario/mSmallRightWalk2.png"));
        marioSprites[M_SMALL_RIGHT_JUMP-M_START] = ImageIO.read(new File("res/mario/mSmallRightJump.png"));
        marioSprites[M_SMALL_RIGHT_FLAG-M_START] = ImageIO.read(new File("res/mario/mSmallRightFlag.png"));
        marioSprites[M_SMALL_RIGHT_TRANSITION-M_START] = ImageIO.read(new File("res/mario/mSmallRightTransition.png"));
        marioSprites[M_SMALL_LEFT_IDLE-M_START] = ImageIO.read(new File("res/mario/mSmallLeftIdle.png"));
        marioSprites[M_SMALL_LEFT_WALK1-M_START] = ImageIO.read(new File("res/mario/mSmallLeftWalk1.png"));
        marioSprites[M_SMALL_LEFT_WALK2-M_START] = ImageIO.read(new File("res/mario/mSmallLeftWalk2.png"));
        marioSprites[M_SMALL_LEFT_JUMP-M_START] = ImageIO.read(new File("res/mario/mSmallLeftJump.png"));
        marioSprites[M_SMALL_LEFT_FLAG-M_START] = ImageIO.read(new File("res/mario/mSmallLeftFlag.png"));
        marioSprites[M_SMALL_LEFT_TRANSITION-M_START] = ImageIO.read(new File("res/mario/mSmallLefttransition.png"));

        marioSprites[M_BIG_RIGHT_IDLE-M_START] = ImageIO.read(new File("res/mario/mBigRightIdle.png"));
        marioSprites[M_BIG_RIGHT_WALK1-M_START] = ImageIO.read(new File("res/mario/mBigRightWalk1.png"));
        marioSprites[M_BIG_RIGHT_WALK2-M_START] = ImageIO.read(new File("res/mario/mBigRightWalk2.png"));
        marioSprites[M_BIG_RIGHT_JUMP-M_START] = ImageIO.read(new File("res/mario/mBigRightJump.png"));
        marioSprites[M_BIG_RIGHT_FLAG-M_START] = ImageIO.read(new File("res/mario/mBigRightFlag.png"));
        marioSprites[M_BIG_LEFT_IDLE-M_START] = ImageIO.read(new File("res/mario/mBigLeftIdle.png"));
        marioSprites[M_BIG_LEFT_WALK1-M_START] = ImageIO.read(new File("res/mario/mBigLeftWalk1.png"));
        marioSprites[M_BIG_LEFT_WALK2-M_START] = ImageIO.read(new File("res/mario/mBigLeftWalk2.png"));
        marioSprites[M_BIG_LEFT_JUMP-M_START] = ImageIO.read(new File("res/mario/mBigLeftJump.png"));
        marioSprites[M_BIG_LEFT_FLAG-M_START] = ImageIO.read(new File("res/mario/mBigLeftFlag.png"));

        marioSprites[M_FIRE_RIGHT_IDLE-M_START] = ImageIO.read(new File("res/mario/mFireRightIdle.png"));
        marioSprites[M_FIRE_RIGHT_WALK1-M_START] = ImageIO.read(new File("res/mario/mFireRightWalk1.png"));
        marioSprites[M_FIRE_RIGHT_WALK2-M_START] = ImageIO.read(new File("res/mario/mFireRightWalk2.png"));
        marioSprites[M_FIRE_RIGHT_JUMP-M_START] = ImageIO.read(new File("res/mario/mFireRightJump.png"));
        marioSprites[M_FIRE_RIGHT_FLAG-M_START] = ImageIO.read(new File("res/mario/mFireRightFlag.png"));
        marioSprites[M_FIRE_LEFT_IDLE-M_START] = ImageIO.read(new File("res/mario/mFireLeftIdle.png"));
        marioSprites[M_FIRE_LEFT_WALK1-M_START] = ImageIO.read(new File("res/mario/mFireLeftWalk1.png"));
        marioSprites[M_FIRE_LEFT_WALK2-M_START] = ImageIO.read(new File("res/mario/mFireLeftWalk2.png"));
        marioSprites[M_FIRE_LEFT_JUMP-M_START] = ImageIO.read(new File("res/mario/mFireLeftJump.png"));
        marioSprites[M_FIRE_LEFT_FLAG-M_START] = ImageIO.read(new File("res/mario/mFireLeftFlag.png"));

        rescaleSprites(marioSprites);
    }

    private static void loadPickUpSprites() throws IOException{
        pickUps = new BufferedImage[PICKUP_COUNT];
        pickUps[P_EMPTY - P_START] = null;
        pickUps[P_COIN-P_START] = ImageIO.read(new File("res/objects/oCoin.png"));
        pickUps[P_MOOSHROOM-P_START] = ImageIO.read(new File("res/objects/oShroom.png"));
        pickUps[P_FLOWER-P_START] = ImageIO.read(new File("res/objects/oFlower.png"));
        pickUps[P_LIFE-P_START] = ImageIO.read(new File("res/objects/oShroomLife.png"));
        pickUps[P_STAR-P_START] = ImageIO.read(new File("res/objects/oStar.png"));
        pickUps[P_FLAG - P_START] = ImageIO.read(new File("res/objects/oFlag.png"));
        pickUps[P_HAMMER - P_START] = ImageIO.read(new File("res/objects/oHammer.png"));

        rescaleSprites(pickUps);
    }

    private static void loadEnemiesSprites() throws IOException{
        enemies = new BufferedImage[ENEMY_COUNT];
        enemies[G_RIGHT - ENEMY_START] = ImageIO.read(new File("res/enemies/eGoombaRight.png"));
        enemies[G_LEFT - ENEMY_START] = ImageIO.read(new File("res/enemies/eGoombaLeft.png"));
        enemies[G_SMASH - ENEMY_START] = ImageIO.read(new File("res/enemies/eGoombaSmash.png"));
        enemies[G_FLIP - ENEMY_START] = ImageIO.read(new File("res/enemies/eGoombaFlip.png"));

        enemies[K_NORMAL_RIGHT_WALK1 - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaRight1.png"));
        enemies[K_NORMAL_RIGHT_WALK2 - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaRight2.png"));
        enemies[K_NORMAL_LEFT_WALK1 - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaLeft1.png"));
        enemies[K_NORMAL_LEFT_WALK2 - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaLeft2.png"));
        enemies[K_FLY_RIGHT_WALK1 - ENEMY_START] = ImageIO.read(new File("res/enemies/eFlyingRight1.png"));
        enemies[K_FLY_RIGHT_WALK2 - ENEMY_START] = ImageIO.read(new File("res/enemies/eFlyingRight2.png"));
        enemies[K_FLY_LEFT_WALK1 - ENEMY_START] = ImageIO.read(new File("res/enemies/eFlyingLeft1.png"));
        enemies[K_FLY_LEFT_WALK2 - ENEMY_START] = ImageIO.read(new File("res/enemies/eFlyingLeft2.png"));
        enemies[K_NORMAL_FLIP - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaFlip.png"));
        enemies[K_SHELL_NORMAL - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaShell.png"));
        enemies[K_SHELL_FLIP - ENEMY_START] = ImageIO.read(new File("res/enemies/eKoopaShellFlip.png"));

        enemies[PI_OPEN - ENEMY_START] = ImageIO.read(new File("res/enemies/ePiranhaOpen.png"));
        enemies[PI_CLOSE - ENEMY_START] = ImageIO.read(new File("res/enemies/ePiranhaClose.png"));

        enemies[BOWSER1 - ENEMY_START] = ImageIO.read(new File("res/enemies/eBowser1.png"));
        enemies[BOWSER2 - ENEMY_START] = ImageIO.read(new File("res/enemies/eBowser2.png"));
        enemies[BOWSER3 - ENEMY_START] = ImageIO.read(new File("res/enemies/eBowser3.png"));
        enemies[BOWSER4 - ENEMY_START] = ImageIO.read(new File("res/enemies/eBowser4.png"));
        
        rescaleSprites(enemies);
    }

    private static void loadFireballSprites() throws IOException{
        fireball = new BufferedImage[FIREBALL_COUNT];
        fireball[FIREBALL_1 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFireball1.png"));
        fireball[FIREBALL_2 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFireball2.png"));
        fireball[FIREBALL_3 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFireball3.png"));
        fireball[FIREBALL_4 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFireball4.png"));
        fireball[FIREBALL_E1 - FIREBALL_START] = ImageIO.read(new File("res/objects/oExplosion1.png"));
        fireball[FIREBALL_E2 - FIREBALL_START] = ImageIO.read(new File("res/objects/oExplosion2.png"));
        fireball[FIREBALL_E3 - FIREBALL_START] = ImageIO.read(new File("res/objects/oExplosion3.png"));
        fireball[FIREBALL_STRIP - FIREBALL_START] = ImageIO.read(new File("res/objects/oFireStrip.png"));
        fireball[FIREBALL_ENEMY1 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFire1.png"));
        fireball[FIREBALL_ENEMY2 - FIREBALL_START] = ImageIO.read(new File("res/objects/oFire2.png"));

        rescaleSprites(fireball);
    }
    
    private static void rescaleSprites(BufferedImage[] images) {
        for (int i = 0; i < images.length; i++) {
            if(images[i]!=null){
                int newHeight = WindowManager.WINDOW_HEIGHT * images[i].getHeight() / PREVIOUS_WINDOW_HEIGHT;
                int newWidth = images[i].getWidth() * newHeight / images[i].getHeight();
                images[i] = createRescaledImage(images[i], newWidth, newHeight);
            }
        }
    }

    private static void rescaleSprites(BufferedImage[] toResize, int width, int height) {
        for (int i = 0; i < toResize.length; i++) {
            if (toResize[i] != null) {
                toResize[i] = createRescaledImage(toResize[i], width, height);
            }
        }
    }

    private static BufferedImage createRescaledImage(Image original, int newWidth, int newHeight) {
        BufferedImage rescaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Image image = original.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        rescaledImage.getGraphics().drawImage(image, 0, 0, null);
        return rescaledImage;
    }

    public static BufferedImage getMarioSprite(int id){
        return marioSprites[id];
    }

    public static BufferedImage getLogo() {
        return gameLogo;
    }

    public static BufferedImage getMiniCoin(){
        return miniCoin;
    }

    public static BufferedImage getBackground(String name) {
        switch (name) {
            case "menu":
                return backgrounds[BACKGROUND_MENU];
            case "lvl1":
                return backgrounds[BACKGROUND_LVL1];
            case "lvl2":
                return backgrounds[BACKGROUND_LVL2];
            case "lvl3":
                return backgrounds[BACKGROUND_LVL3];
            case "lvl4":
                return backgrounds[BACKGROUND_LVL4];
            default:
                throw new NoSuchElementException();
        }

    }

    public static BufferedImage getBlockSprite(int id) {
        return blockSprites[id];
    }

    public static BufferedImage getPickUpSprite(int id){
        return pickUps[id];
    }

    public static BufferedImage getEnemySprite(int id){
        return enemies[id];
    }

    public static BufferedImage getFireballSprite(int id){
        return fireball[id];
    }

}
