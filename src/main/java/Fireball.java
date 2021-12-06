package main.java;

import static main.java.MarioController.RIGHT;

import main.java.Enemies.Enemy;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.Supplier;

public class Fireball extends PhysicObject{
    public static final int MARIO_FIRE = 0;
    public static final int STRIP_FIRE = 2;
    public static final int ENEMY_FIRE = Enemy.FIRE;

    private static final int MARIO_FIRE_SPEED = 9;
    private static final int ENEMY_FIRE_SPEED = 5;
    private static final int CAMERA_OFFSET = GameRunner.instance.cameraOffset * 2;
    
    protected Supplier<Boolean> tickMethod;
    protected Hashtable<String, Integer> movingSprites;
    protected Hashtable<String, Integer> explosionSprites;

    protected static Mario mario;

    protected int id;
    protected int direction;
    protected int currentSprite;
    protected int spriteCounter;
    protected int behaviorCounter;
    protected boolean active;
    protected boolean exploting;

    public Fireball(){}

    public Fireball(Point position, boolean direction, int id){
        this.id = id;
        this.direction = (direction==RIGHT) ? 1 : -1; 
        spriteCounter = 0;
        mario = Mario.getCurrentInstance();
        setLocation(position);
        defineTypeProperties();
    }

    private void defineTypeProperties(){
        movingSprites = new Hashtable<>(8);
        explosionSprites = new Hashtable<>(6);
        explosionSprites.put("exp1", Animator.FIREBALL_E1);
        explosionSprites.put("exp2", Animator.FIREBALL_E2);
        explosionSprites.put("exp3", Animator.FIREBALL_E3);
        switch(id){
            case MARIO_FIRE:
                tickMethod = () -> marioFire();
                currentSprite = Animator.FIREBALL_1;
                movingSprites.put("move1", Animator.FIREBALL_1);
                movingSprites.put("move2", Animator.FIREBALL_2);
                movingSprites.put("move3", Animator.FIREBALL_3);
                movingSprites.put("move4", Animator.FIREBALL_4);
                horizontalVelocity = MARIO_FIRE_SPEED * direction;
                verticalVelocity = 5;
                hCollisionOffset = Math.abs(horizontalVelocity);
                vCollisionOffset = verticalVelocity;
                active = true;
                Sound.makeSound(Sound.FIREBALL);
                setColliderSize(Animator.getFireballSprite(currentSprite));
                break;
            case ENEMY_FIRE:
                tickMethod = () -> enemyFire();
                currentSprite = Animator.FIREBALL_ENEMY1;
                movingSprites.put("move1", Animator.FIREBALL_ENEMY1);
                movingSprites.put("move2", Animator.FIREBALL_ENEMY2);
                movingSprites.put("move3", Animator.FIREBALL_ENEMY1);
                movingSprites.put("move4", Animator.FIREBALL_ENEMY2);
                horizontalVelocity = ENEMY_FIRE_SPEED * direction;
                verticalVelocity = 0;
                hCollisionOffset = Math.abs(horizontalVelocity);
                vCollisionOffset = 1;
                active = false;
                setColliderSize(Animator.getFireballSprite(currentSprite));
                break;
            case STRIP_FIRE:
                active = false;
                break;
            default:
                break;
        }
    }

    public void paintFireball(Graphics g){
        super.paint(g);
        g.drawImage(Animator.getFireballSprite(currentSprite), x, y, GameRunner.instance);
    }

    public void tick(ArrayList<Enemy> enemies){
        if (mario.isTransitioning() || !mario.isAlive()) {
            return;
        }

        if(!active){
            checkForActivation();
            return;
        }

        if(!exploting){
            checkCollisions();
            tickMethod.get();
            updateSprite();
            enemiesCollisions(enemies);
            return;
        }
        updateExplosion();       
    }

    protected void checkForActivation(){
        // Check if is visible
        if (x - CAMERA_OFFSET <= mario.x || x <= WindowManager.windowWidth) {
            if(id != STRIP_FIRE){
                Sound.makeSound(Sound.BOWSER_FIRE);
            }
            active = true;
        }
    }

    private void updateSprite(){
        if(spriteCounter < 10){
            currentSprite = movingSprites.get("move1");
        }else if(spriteCounter < 20){
            currentSprite = movingSprites.get("move2");
        } else if (spriteCounter < 30) {
            currentSprite = movingSprites.get("move3");
        } else if (spriteCounter < 40) {
            currentSprite = movingSprites.get("move4");
        }else{
            spriteCounter = 0;
        }
        spriteCounter ++;
    }

    private void updateExplosion(){
        if (spriteCounter < 5) {
            currentSprite = explosionSprites.get("exp1");
        } else if (spriteCounter < 10) {
            currentSprite = explosionSprites.get("exp2");
        } else if (spriteCounter < 15) {
            currentSprite = explosionSprites.get("exp3");
        } else {
            destroyFireball();
        }

        if(spriteCounter % 5 == 0){
            x -= Animator.getFireballSprite(Animator.FIREBALL_E1).getWidth() / 2;
            y -= Animator.getFireballSprite(Animator.FIREBALL_E1).getHeight() / 2;
        }
        spriteCounter++;
    }
    
    private void enemiesCollisions(ArrayList<Enemy> enemies){
        if(id != MARIO_FIRE){
            return;
        }

        for(Enemy e : enemies){
            if (e.isInteractable() && intersects(e)) {
                if (e.getId() != Enemy.getBowserId()) {
                    e.killFlip();
                }
                startExplosion();
            }
        }
    }

    private boolean marioFire(){
        if(behaviorCounter == 15){
            verticalVelocity = 5;
        }else if(behaviorCounter >= 15){
            behaviorCounter = 0;
        }
        behaviorCounter++;

        if (collisions[PhysicObject.COLLISION_BOTTOM]) {
            verticalVelocity = -5;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_TOP]) {
            verticalVelocity = 5;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            startExplosion();
        }

        setLocation(x + horizontalVelocity, y + verticalVelocity);
        return true;
    }

    private boolean enemyFire() {
        checkMarioCollisions();

        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            startExplosion();
        }
        setLocation(x + horizontalVelocity, y);
        return true;
    }

    protected void checkMarioCollisions(){
        if(intersects(mario)){
            mario.applyDamage();
        }
    }

    private void startExplosion(){
        Sound.makeSound(Sound.BLOCK_HIT);
        spriteCounter = 0;
        exploting = true;
    }
    
    public void destroyFireball() {
        LevelMap.deleteObject(this);
    }
}
