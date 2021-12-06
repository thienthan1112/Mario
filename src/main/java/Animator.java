package main.java;

import java.awt.image.BufferedImage;

public class Animator {
    public static final int ANIMATION_SPEED = 6;

    public static final int M_START = 0;
    public static final int EMPTY = 0;
    public static final int M_DEAD = 1;
    public static final int M_SMALL_RIGHT_IDLE = 2;
    public static final int M_SMALL_RIGHT_WALK1 = 3;
    public static final int M_SMALL_RIGHT_WALK2 = 4;
    public static final int M_SMALL_RIGHT_JUMP = 5;
    public static final int M_SMALL_RIGHT_FLAG = 6;
    public static final int M_SMALL_RIGHT_TRANSITION = 7;
    public static final int M_SMALL_LEFT_IDLE = 8;
    public static final int M_SMALL_LEFT_WALK1 = 9;
    public static final int M_SMALL_LEFT_WALK2 = 10;
    public static final int M_SMALL_LEFT_JUMP = 11;
    public static final int M_SMALL_LEFT_FLAG = 12;
    public static final int M_SMALL_LEFT_TRANSITION = 13;

    public static final int M_BIG_RIGHT_IDLE = 14;
    public static final int M_BIG_RIGHT_WALK1 = 15;
    public static final int M_BIG_RIGHT_WALK2 = 16;
    public static final int M_BIG_RIGHT_JUMP = 17;
    public static final int M_BIG_RIGHT_FLAG = 18;
    public static final int M_BIG_LEFT_IDLE = 19;
    public static final int M_BIG_LEFT_WALK1 = 20;
    public static final int M_BIG_LEFT_WALK2 = 21;
    public static final int M_BIG_LEFT_JUMP = 22;
    public static final int M_BIG_LEFT_FLAG = 23;

    public static final int M_FIRE_RIGHT_IDLE = 24;
    public static final int M_FIRE_RIGHT_WALK1 = 25;
    public static final int M_FIRE_RIGHT_WALK2 = 26;
    public static final int M_FIRE_RIGHT_JUMP = 27;
    public static final int M_FIRE_RIGHT_FLAG = 28;
    public static final int M_FIRE_LEFT_IDLE = 29;
    public static final int M_FIRE_LEFT_WALK1 = 30;
    public static final int M_FIRE_LEFT_WALK2 = 31;
    public static final int M_FIRE_LEFT_JUMP = 32;
    public static final int M_FIRE_LEFT_FLAG = 33;

    public static final int MARIO_SPRITE_COUNT = 34;

    
    public static final int P_START = 50;
    public static final int P_EMPTY = 50;
    public static final int P_COIN = 51;
    public static final int P_MOOSHROOM = 52;
    public static final int P_FLOWER = 53;
    public static final int P_LIFE = 54;
    public static final int P_STAR = 55;
    public static final int P_FLAG = 56;
    public static final int P_HAMMER = 57;
    public static final int PICKUP_COUNT = 8;

    public static final int ENEMY_START = 101;
    public static final int G_RIGHT = 101;
    public static final int G_LEFT = 102;
    public static final int G_SMASH = 103;
    public static final int G_FLIP = 104;
    public static final int K_NORMAL_RIGHT_WALK1 = 105;
    public static final int K_NORMAL_RIGHT_WALK2 = 106;
    public static final int K_NORMAL_LEFT_WALK1 = 107;
    public static final int K_NORMAL_LEFT_WALK2 = 108;
    public static final int K_FLY_RIGHT_WALK1 = 109;
    public static final int K_FLY_RIGHT_WALK2 = 110;
    public static final int K_FLY_LEFT_WALK1 = 111;
    public static final int K_FLY_LEFT_WALK2 = 112;
    public static final int K_NORMAL_FLIP = 113;
    public static final int K_SHELL_NORMAL = 114;
    public static final int K_SHELL_FLIP = 115;
    public static final int PI_OPEN = 116;
    public static final int PI_CLOSE = 117;
    public static final int BOWSER1 = 118;
    public static final int BOWSER2 = 119;
    public static final int BOWSER3 = 120;
    public static final int BOWSER4 = 121;

    public static final int ENEMY_COUNT = 21;

    public static final int FIREBALL_START = 201;
    public static final int FIREBALL_1 = 201;
    public static final int FIREBALL_2 = 202;
    public static final int FIREBALL_3 = 203;
    public static final int FIREBALL_4 = 204;
    public static final int FIREBALL_E1 = 205;
    public static final int FIREBALL_E2 = 206;
    public static final int FIREBALL_E3 = 207;
    public static final int FIREBALL_STRIP = 208;
    public static final int FIREBALL_ENEMY1 = 209;
    public static final int FIREBALL_ENEMY2 = 210;
    
    public static final int FIREBALL_COUNT = 10;

    public static BufferedImage getMarioSprite(int id){
        return SpriteAssets.getMarioSprite(id-M_START);
    }

    public static BufferedImage getBlockSprite(int id){
        return SpriteAssets.getBlockSprite(id);
    }

    public static BufferedImage getPickUpSprite(int id){
        return SpriteAssets.getPickUpSprite(id-P_START);
    }

    public static BufferedImage getEnemySprite(int id){
        return SpriteAssets.getEnemySprite(id-ENEMY_START);
    }

    public static BufferedImage getFireballSprite(int id){
        return SpriteAssets.getFireballSprite(id-FIREBALL_START);
    }

}
