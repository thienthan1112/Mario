package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Score {
    private static Score instance;
    private static BufferedImage miniCoin;
    private static BufferedImage marioSprite;
    private Font textFont;

    private long prevSecond;
    private int points;
    private int coins;
    private int time;
    private int world;
    private int level;
    private int lives;
    private int middleScreen;

    private boolean addingPoints;
    
    public Score(){
        instance = this;
        world = 1;
        level = 1;
        lives = 3;
        textFont = TextFont.getFont();
        textFont = textFont.deriveFont(Font.PLAIN, 24);
        if(miniCoin==null){
            miniCoin = SpriteAssets.getMiniCoin();
        }
        if(marioSprite==null){
            marioSprite = SpriteAssets.getMarioSprite(Animator.M_SMALL_RIGHT_IDLE);
        }
    }


    public void tick(){
        if(addingPoints){
            if(time > 0){
                points+=20;
                time--;
                Sound.makeSound(Sound.COIN);
            }else if (time==0){
                time = -1;
                GameRunner.instance.requestNextLevel();
            }
            return;
        }
        if(System.currentTimeMillis() >= prevSecond + 1000){
            time--;
            prevSecond = System.currentTimeMillis();
            if(time == 45){
                Sound.makeSound(Sound.TIME_WARNING);
            }
        }
    }

    public void paint(Graphics g, int offset){
        middleScreen = WindowManager.windowWidth / 2 + offset;
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString("MARIO", middleScreen - 300, 40);
        g.drawString(String.format("%06d", points), middleScreen - 300, 65);
        g.drawImage(miniCoin, middleScreen - 120, 40, GameRunner.instance);
        g.drawString(String.format("x%02d", coins), middleScreen - 100, 65);
        g.drawString("WORLD", middleScreen + 30, 40);
        g.drawString(String.format("%d-%d", world, level), middleScreen + 53, 65);
        g.drawString("TIME", middleScreen + 205, 40);
        if(time >= 0){
            g.drawString(String.format("%03d", time), middleScreen + 230, 65);
        }else{
            g.drawString(String.format("%03d", 0), middleScreen + 230, 65);
        }
        
    }

    public void paintFullDetails(Graphics g, int lvlId){
        g.setColor(Color.BLACK);
        g.fillRect(middleScreen-WindowManager.windowWidth/2, 0, WindowManager.windowWidth, WindowManager.WINDOW_HEIGHT);
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString(String.format("WORLD  %d-%d", world, level), middleScreen - 130, 300);
        if(lives>0){
            g.drawImage(marioSprite, middleScreen - 100, 350, GameRunner.instance);
            g.drawString(String.format("x  %d", lives), middleScreen - 10, 405);
        }else{
            g.drawString("GAME OVER", middleScreen - 115, 405);
        }
            
    }

    public void addTimeToPoints(){
        addingPoints = true;
    }

    public void stopAddingTimeToPoints(){
        addingPoints = false;
    }

    public static Score getInstance(){
        return instance;
    }

    public void addToPoints(int toAdd) {
        points += toAdd;
    }

    public void addToCoins(int toAdd) {
        coins += toAdd;
        if (coins >= 100) {
            increaseLives();
            coins = 0;
        }else{
            Sound.makeSound(Sound.COIN);
        }
    }

    public void increaseLives() {
        Sound.makeSound(Sound.LIVE_UP);
        lives++;
    }

    public void decreaseLives() {
        lives--;
    }

    public int getLives(){
        return lives;
    }

    public void setTimer(int time) {
        if(time > 0){
            addingPoints = false;
        }
        this.time = time;
    }

    public int getTimer() {
        return time;
    }

    public void setWorld(int w) {
        world = w;
    }

    public void setLevel(int l) {
        level = l;
    }
}
