package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.util.function.Supplier;

import main.java.Mario.MarioState;

public class PickUp extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int COIN = 0;
    private static final int MOOSHROOM = 1;
    private static final int FLOWER = 2;
    private static final int LIFE = 3;
    //private static final int STAR = 4;
    private static final int FLAG = 5;
    private static final int GOAL = 6;
    private static final int COIN_STATIC = 7;
    private static final int HAMMER = 8;

    private static Score scoreManager;
    private static Mario mario;

    private Supplier<Boolean> tickMethod;
    private Type type;
    private int stateCounter;
    private int id;
    private int sprite=Animator.P_EMPTY;
    private int sound;
    
    private boolean active;

    public PickUp(Type type, Point position){
        this.type = type;
        canActivateBlocks = false;
        defineTypeProperties(position);
        setScoreManager();
        mario = Mario.getCurrentInstance();
    }

    private void setScoreManager() {
        if (scoreManager == null) {
            scoreManager = Score.getInstance();
        }
    }

    private void defineTypeProperties(Point position){
        switch (type) {
            case COIN:
                tickMethod = () -> coinTick();
                verticalVelocity = 5;
                id = COIN;
                sprite = Animator.P_COIN;
                sound = -1;
                break;
            case COIN_MULTIPLE:
                tickMethod = () -> coinTick();
                id = COIN;
                verticalVelocity = 5;
                sprite = Animator.P_COIN;
                sound = -1;
                break;
            case POWER:
                //Type of powerup will change depending on Mario state
                tickMethod = () -> powerTick();
                id = -1;
                sound = Sound.POWER_APPEAR;
                break;
            case LIFE:
                tickMethod = () -> lifeTick();
                id = LIFE;
                verticalVelocity = PhysicObject.getGravity();
                horizontalVelocity = 3;
                sprite = Animator.P_LIFE;
                hCollisionOffset = horizontalVelocity;
                vCollisionOffset = verticalVelocity * 2;
                setColliderSize(Animator.getPickUpSprite(Animator.P_LIFE));
                sound = Sound.POWER_APPEAR;
                break;
            case FLAG:
                tickMethod = () -> flagTick();
                id = FLAG;
                sprite = Animator.P_FLAG;
                x = position.x - Block.SIZE/2;
                y = position.y;
                active = true;
                setColliderSize(Animator.getPickUpSprite(Animator.P_FLAG));
                sound = -1;
                break;
            case GOAL:
                tickMethod = () -> goalTick();
                id = GOAL;
                sprite = Animator.P_EMPTY;
                setLocation(position);
                active = true;
                setColliderSize(Animator.getPickUpSprite(Animator.P_FLOWER));
                sound = -1;
                break;
            case COIN_STATIC:
                tickMethod = () -> coinStaticTick();
                id = COIN_STATIC;
                setLocation(position);
                sprite = Animator.P_COIN;
                active = true;
                setColliderSize(Animator.getPickUpSprite(Animator.P_COIN));
                sound = -1;
                break;
            case HAMMER:
                tickMethod = () -> hammerTick();
                id = HAMMER;
                setLocation(position);
                sprite = Animator.P_HAMMER;
                active = true;
                setColliderSize(Animator.getPickUpSprite(Animator.P_HAMMER));
                sound = -1;
                break;
            default:
                break;
        }
    }
    
    public void spawnPickUp(Point p) {
        
        x = p.x;
        y = p.y + Block.SIZE/2;
        active = true;
        Sound.makeSound(sound);
        if(type == Type.POWER){
            definePowerBehavoir();
        }
    }

    private void definePowerBehavoir(){
        if (mario.state == MarioState.SMALL) {
            id = MOOSHROOM;
            sprite = Animator.P_MOOSHROOM;
            verticalVelocity = PhysicObject.getGravity();
            horizontalVelocity = 3;
            hCollisionOffset = horizontalVelocity;
            vCollisionOffset = verticalVelocity * 2;
            setColliderSize(Animator.getPickUpSprite(Animator.P_MOOSHROOM));
        } else {
            id = FLOWER;
            sprite = Animator.P_FLOWER;
            setColliderSize(Animator.getPickUpSprite(Animator.P_FLOWER));
            
        }
        
    }

    public void paintPickUp(Graphics g){
        if (active) {    
            super.paint(g);
            if(sprite == -50){
                System.out.println(type);
            }
            g.drawImage(Animator.getPickUpSprite(sprite), x, y, GameRunner.instance);
        }
    }

    public void tick(){
        if (mario.isTransitioning() || !mario.isAlive()) {
            return;
        }

        if(active){
            tickMethod.get();
        }
    }

    private boolean coinTick() {
        if (stateCounter < 10) {
            y -= verticalVelocity;
            stateCounter++;
        } else if (stateCounter < 50) {
            scoreManager.addToCoins(1);
            scoreManager.addToPoints(100);
            active = false;
            LevelMap.deleteObject(this);
        }
        
        return true;
    }

    private boolean powerTick(){
        if (stateCounter < Block.SIZE / 2 + 1) {
            y -= 1;
            stateCounter++;
        }else if(id == MOOSHROOM || id == LIFE){
            isFalling = verticalVelocity > 0;
            applyVelocities();
            checkCollisions();
        }
        
        checkMarioCollision();
        
        return true;
    }

    private boolean lifeTick(){
        powerTick();
        return true;
    }
    
    private boolean flagTick(){
        if(stateCounter == 0){
            if(intersects(mario) && !mario.hasFinishLevel()){
                scoreManager.increaseLives();
                stateCounter = 1;
            }
        }
        return true;
    }

    private boolean goalTick(){
        if(mario.x > x + width/2){
            mario.hide();
            active = false;
            GameRunner.instance.requestNextLevel();
        }
        return true;
    }

    private boolean coinStaticTick(){
        checkMarioCollision();
        return true;
    }
    
    private boolean hammerTick(){
        checkMarioCollision();
        return true;
    }

    private void checkMarioCollision(){
        if(!mario.isAlive()){
            return;
        }
        if(intersects(mario)){
            switch(id){
                case MOOSHROOM:
                    mario.applyMooshroom();
                    scoreManager.addToPoints(1000);
                    break;
                case FLOWER:
                    mario.applyFire();
                    scoreManager.addToPoints(1000);
                    break;
                case LIFE:
                    scoreManager.increaseLives();
                    break;
                case COIN_STATIC:
                    scoreManager.addToCoins(1);
                    scoreManager.addToPoints(100);
                    break;
                case HAMMER:
                    GameRunner.instance.requestNextLevel();
                    break;
            }
            active = false;
            LevelMap.deleteObject(this);
        }
    }

    public int getId(){
        return id;
    }

    public static int getFlagId(){
        return FLAG;
    }

    public static boolean mustReplaceBlock(int id){
        return id == Type.GOAL.id || id == Type.COIN_STATIC.id || id == Type.HAMMER.id;
    }

    public enum Type {
        COIN(1), COIN_MULTIPLE(2), POWER(3), LIFE(4), FLAG(5), GOAL(6), COIN_STATIC(7), HAMMER(8);

        private int id;

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Type typeById(int id) {
            for (Type t : values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }


}
