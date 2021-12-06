package main.java;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    public static final int LVL1 = 0;
    public static final int LVL2 = 1;
    public static final int LVL3 = 2;
    public static final int LVL4 = 3;
    public static final int BLOCK_HIT = 4;
    public static final int BLOCK_BREAK = 5;
    public static final int STOMP = 6;
    public static final int POWER_UP = 7;
    public static final int POWER_DOWN = 8;
    public static final int JUMP = 9;
    public static final int KICK = 10;
    public static final int MARIO_DIE = 11;
    public static final int LIVE_UP = 12;
    public static final int PIPE = 13;
    public static final int FIREBALL = 14;
    public static final int COIN = 15;
    public static final int POWER_APPEAR = 16;
    public static final int LEVEL_CLEAR = 17;
    public static final int WORLD_CLEAR = 18;
    public static final int BOWSER_FALL = 19;
    public static final int BOWSER_FIRE = 20;
    public static final int GAME_OVER = 21;
    public static final int TIME_WARNING = 22;
    
    private static final int SOUND_COUNT = 23;

    private static File[] inputs;
    private static Clip[] clips;

    static{
        try{
            loadSounds();
        }catch(Exception e){
            MyLogger.logErrorMessage("The sounds of the game could not be loaded.", e);
        }
        
    }

    private static void loadSounds() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
        clips = new Clip[SOUND_COUNT];
        for(int i = 0; i < clips.length; i++){
            clips[i] = AudioSystem.getClip();
        }

        inputs = new File[SOUND_COUNT];

        inputs[LVL1] = new File("res/sounds/lvl1.wav");
        inputs[LVL2] = new File("res/sounds/lvl2.wav");
        inputs[LVL3] = new File("res/sounds/lvl1.wav");
        inputs[LVL4] = new File("res/sounds/lvl4.wav");
        inputs[BLOCK_HIT] = new File("res/sounds/bump.wav");
        inputs[BLOCK_BREAK] = new File("res/sounds/breakblock.wav");
        inputs[STOMP] = new File("res/sounds/stomp.wav");
        inputs[POWER_UP] = new File("res/sounds/powerup.wav");
        inputs[POWER_DOWN] = new File("res/sounds/powerdown.wav");
        inputs[JUMP] = new File("res/sounds/jump.wav");
        inputs[KICK] = new File("res/sounds/kick.wav");
        inputs[MARIO_DIE] = new File("res/sounds/mariodie.wav");
        inputs[LIVE_UP] = new File("res/sounds/oneup.wav");
        inputs[PIPE] = new File("res/sounds/pipe.wav");
        inputs[FIREBALL] = new File("res/sounds/fireball.wav");
        inputs[COIN] = new File("res/sounds/coin.wav");
        inputs[POWER_APPEAR] = new File("res/sounds/powerup_appears.wav");
        inputs[LEVEL_CLEAR] = new File("res/sounds/stageclear.wav");
        inputs[WORLD_CLEAR] = new File("res/sounds/world_clear.wav");
        inputs[BOWSER_FALL] = new File("res/sounds/bowserfalls.wav");
        inputs[BOWSER_FIRE] = new File("res/sounds/bowserfire.wav");
        inputs[GAME_OVER] = new File("res/sounds/gameover.wav");
        inputs[TIME_WARNING] = new File("res/sounds/warning.wav");
    }

    public static void makeSound(int id){
        if(id<0)
            return;

        try{
            clips[id] = AudioSystem.getClip();
            clips[id].open(AudioSystem.getAudioInputStream(inputs[id]));
            clips[id].start();
        }catch(Exception e){
            MyLogger.logWarningMessage("The sound " + id + " could not be played.", e);
        }
    }

    public static void loopSound(int id){
        makeSound(id);
        clips[id].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stopSound(int id){
        clips[id].stop();
    }

    public static void stopAllSounds(){
        for(int i = 0; i<SOUND_COUNT; i++){
            if(i != LIVE_UP){
                clips[i].stop();
            }     
        }
    }
}
