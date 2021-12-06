package main.java;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Rectangle {

    private static final long serialVersionUID = 1L;

    public static final int EMPTY = 0;
    public static final int GROUND = 1;
    public static final int BREAKABLE = 2;
    public static final int MISTERY = 3;
    public static final int SOLID = 4;
    public static final int PIPE_B_LEFT = 5;
    public static final int PIPE_B_RIGHT = 6;
    public static final int PIPE_T_LEFT = 7;
    public static final int PIPE_T_RIGHT = 8;
    public static final int USED = 9;
    public static final int FLAG_POST = 10;
    public static final int FLAG_TOP = 11;

    public static final int BREAKABLE_ANIM1 = 13;
    public static final int BREAKABLE_ANIM2 = 14;
    public static final int BREAKABLE_ANIM3 = 15;
    public static final int BREAKABLE_DARK = 16;
    public static final int GROUND_DARK = 17;
    public static final int SOLID_DARK = 18;

    public static final int ENEMY_AI = 19;

    public static final int PIPEH_B_LEFT = 20;
    public static final int PIPEH_B_RIGHT = 21;
    public static final int PIPEH_T_LEFT = 22;
    public static final int PIPEH_T_RIGHT = 23;

    public static final int GRASS_LEFT = 24;
    public static final int GRASS_CENTER = 25;
    public static final int GRASS_RIGHT = 26;
    public static final int GRASS_SUPPORT = 27;

    public static final int CASTLE_GROUND = 28;
    public static final int CASTLE_BRIDGE = 29;

    public static final int BLOCK_COUNT = 30;

    public static final int SIZE = WindowManager.WINDOW_HEIGHT / 14;

    protected BufferedImage currentSprite;
    
    private byte id;
    protected boolean collision;

    public Block(Point position, int id) {
        this.id = (byte) id;
        if(this.id == GRASS_SUPPORT){
            collision = false;
        }else{
            collision = true;
        }
        currentSprite = SpriteAssets.getBlockSprite(id);
        setBounds(position.x, position.y, SIZE, SIZE);
    }

    public void darkMode(){
        switch(id){
        case BREAKABLE:
            currentSprite = SpriteAssets.getBlockSprite(BREAKABLE_DARK);
            break;
        case GROUND:
            currentSprite = SpriteAssets.getBlockSprite(GROUND_DARK);
            break;
        case SOLID:
            currentSprite = SpriteAssets.getBlockSprite(SOLID_DARK);
            break;
        }
    }

    public void lightMode() {
        switch (id) {
        case BREAKABLE:
            currentSprite = SpriteAssets.getBlockSprite(BREAKABLE);
            break;
        case GROUND:
            currentSprite = SpriteAssets.getBlockSprite(GROUND);
            break;
        case SOLID:
            currentSprite = SpriteAssets.getBlockSprite(SOLID);
            break;
        }
    }

    public void paintBlock(Graphics g) {
        if (id != EMPTY) {
            g.drawImage(currentSprite, x, y, GameRunner.instance);
        }
    }

    public int getId() {
        return id;
    }

    public void deactivateBlock(){
        if(id==Block.BREAKABLE){
            currentSprite = SpriteAssets.getBlockSprite(EMPTY);
            collision = false;
            return;
        }
        
        if(id == Block.MISTERY){
            currentSprite = SpriteAssets.getBlockSprite(USED);
            return;
        }
    }

    public boolean isActive(){
        return collision;
    }

    public void hideBlock(){
        id = EMPTY;
    }

    public void activateBlock(){}


}
