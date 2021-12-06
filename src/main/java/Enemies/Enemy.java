package main.java.Enemies;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import main.java.Animator;
import main.java.GameRunner;
import main.java.LevelMap;
import main.java.Mario;
import main.java.PhysicObject;
import main.java.Score;
import main.java.Sound;
import main.java.WindowManager;

public abstract class Enemy extends PhysicObject {
    protected static final long serialVersionUID = 1L;

    protected static final int GOOMBA = 1;
    protected static final int KOOPA = 2;
    protected static final int KOOPA_FLYING = 3;
    protected static final int PIRANHA = 4;
    protected static final int SHELL = 5;
    public static final int FIRE = 6; //Fireball.java needs reference this value
    protected static final int BOWSER = 7;

    protected static final int GOOMBA_VEL = 2;
    protected static final int KOOPA_VEL = 2;
    protected static final int KOOPA_FLYING_VEL = 2;
    protected static final int PIRANHA_VEL = 1;
    protected static final int SHELL_VEL = 6;
    protected static final int BOWSER_VEL = 2;

    private static Score scoreManager;
    private static int cameraOffset = GameRunner.instance.cameraOffset * 2;
    protected Mario mario;

    protected Hashtable<String, Integer> tableSprites;
    protected int id;
    protected int sprite;
    protected int destroyCounter;
    protected int animCounter;
    protected int behaviorCounter;
    protected int jumpCounter;
    protected int destroyTime;
    protected boolean alive;
    protected boolean active;
    protected boolean flipAnimation;

    public static Enemy create(Point position, int id){
        switch (id) {
        case GOOMBA:
            return new Goomba(position, id);
        case KOOPA:
            return new Koopa(position, id);
        case KOOPA_FLYING:
            return new FlyingKoopa(position, id);
        case PIRANHA:
            return new Piranha(position, id);
        case SHELL:
            return new Shell(position, id);
        case BOWSER:
            return new Bowser(position, id);
        default:
            throw new NoSuchElementException("There is no enemy defined for id: " + id);
        }
    }

    protected Enemy(Point position, int id){
        this.id = id;
        tableSprites = new Hashtable<>(12);
        canActivateBlocks = false;
        alive = true;
        scoreManager = Score.getInstance();
        mario = Mario.getCurrentInstance();
    }

    protected abstract void tickEnemy();

    public void paintEnemy(Graphics g){
        if(active){
            super.paint(g);
            g.drawImage(Animator.getEnemySprite(sprite), x, y, GameRunner.instance);
        }
    }

    public void tick(ArrayList<Enemy> enemies){
        if(!active){
            checkForActivation(mario.x);
            return;
        }

        if(mario.isTransitioning() || !mario.isAlive()){
            return;
        }

        if(alive){
            checkEnemiesCollisions(enemies);
            tickEnemy();
        }else{
            destroy();
        }
    }

    private void checkForActivation(int marioXPos){
        //Check if enemy is visible
        if (x - cameraOffset <= marioXPos || x <= WindowManager.windowWidth){
            active = true;
        }
    }

    protected void changeSpriteDirection(){
        if(horizontalVelocity<=0){
            if(animCounter > Animator.ANIMATION_SPEED*2){
                if(sprite == tableSprites.get("left1")){
                    sprite = tableSprites.get("left2");
                }else{
                    sprite = tableSprites.get("left1");
                }
                animCounter = 0;
            }
        }else{
            if (animCounter > Animator.ANIMATION_SPEED * 2) {
                if (sprite == tableSprites.get("right1")) {
                    sprite = tableSprites.get("right2");
                }else{
                    sprite = tableSprites.get("right1");
                }
                animCounter = 0;
            }
        }
        animCounter++;
    }

    protected void checkMarioCollisions() {
        if(intersects(mario)){
            if (id == PIRANHA){
                mario.applyDamage();
                return;
            }
            //Mario is on top of the enemy
            if (mario.y + mario.height <= y + mario.verticalVelocity - verticalVelocity) {
                if (id == KOOPA_FLYING) {
                    replaceEnemy(KOOPA);
                } else if (id == KOOPA) {
                    replaceEnemy(SHELL);
                } else if (id == SHELL) {
                    mario.activateMiniJump();
                    horizontalVelocity = 0;
                } else {
                    mario.activateMiniJump();
                    kill();
                }
                Sound.makeSound(Sound.STOMP);
            } else {
                //Shell not moving will not kill mario
                if (id == SHELL && horizontalVelocity == 0) {
                    int direction = 1;
                    if (mario.x > x + width / 2) {
                        direction = -1;
                    }
                    horizontalVelocity = SHELL_VEL * direction;
                    x+=horizontalVelocity*2;
                    Sound.makeSound(Sound.KICK);
                } else {
                    mario.applyDamage();
                }
            }
        }        
    }

    private void checkEnemiesCollisions(ArrayList<Enemy> enemies){
        for(Enemy e : enemies){
            if(e.isInteractable() && intersects(e) && e!=this){
                if(e.id == SHELL && e.horizontalVelocity != 0){
                    if(id == SHELL && horizontalVelocity != 0){
                        e.killFlip();
                    }
                    killFlip();
                    Sound.makeSound(Sound.STOMP);
                }else{
                    if(id == SHELL && horizontalVelocity!=0){
                        return;
                    }
                    int direction = 1;
                    int offset = e.width;
                    if (e.x > x + width / 2) {
                        offset = - width;
                        direction = -1;
                    }
                    x = e.x + offset;
                    e.horizontalVelocity *= direction;
                    horizontalVelocity *= direction;
                    changeSpriteDirection();
                    e.changeSpriteDirection();
                }
                
            }
        }
    }

    private void replaceEnemy(int id){
        Enemy replacement = Enemy.create(getLocation(), id);
        if(horizontalVelocity != 0){
            replacement.horizontalVelocity = Math.abs(replacement.horizontalVelocity) * Integer.signum(horizontalVelocity);
        }
        mario.activateMiniJump();
        LevelMap.addObject(replacement);
        LevelMap.deleteObject(this);
    }

    private void kill(){
        scoreManager.addToPoints(100);
        sprite = tableSprites.get("dead");
        horizontalVelocity = 0;
        verticalVelocity = 0;
        destroyCounter = 0;
        destroyTime = 30;
        alive = false;      
    }

    public void killFlip(){
        if(id == PIRANHA){
            return;
        }
        kill();
        //Override the sprite and destroyTime set by the kill method
        sprite = tableSprites.get("flip");
        destroyTime = 60;
        flipAnimation = true;
    }

    private void destroy(){
        if(flipAnimation){
            if(destroyCounter<10){
                setLocation(x, y-2);
            }else{
                setLocation(x, y+4);
            }
        }
        if(destroyCounter>destroyTime){
            LevelMap.deleteObject(this);
        }
        destroyCounter++;
    }

    public int getId(){
        return id;
    }

    public boolean isInteractable(){
        return alive && active;
    }

    public static int getFireId(){
        return FIRE;
    }

    public static int getBowserId(){
        return BOWSER;
    }

    public static boolean mustSpawnEnemy(String[] token){
        return !(token.length < 2 || token[0].charAt(0)!='0');
    }

    public static boolean isShell(PhysicObject object){
        try{
            return ((Enemy)object).id == SHELL;
        }catch(ClassCastException e){
            return false;
        }
    }
    
}
