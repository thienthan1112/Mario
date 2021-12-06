package main.java;

import static main.java.Animator.*;
import static main.java.MarioController.RIGHT;
import java.awt.*;
import java.util.Hashtable;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int INVINCIBLE_TICKS = 150;
    private static final int DEAD_TICKS = 320;
    private static Mario instance;

    protected MarioState state;
    private MarioController controller;

    private Hashtable<String, Integer> movingSprites;
    private Hashtable<String, Integer> transitionSprites;
    private PickUp finishFlag;

    private int lastPipeYPosition;
    private int currentSprite;
    private int previousSprite;
    private int currentAnimSpeed;
    private int animationCounter;
    private int transitionCounter;
    private int invincibleCounter;
    private int deadCounter;
    
    private boolean walkingCollisions;
    private boolean canMove;
    private boolean walking;
    private boolean transitioning;
    private boolean invincible;
    private boolean alive;
    private boolean levelFinished;
    private boolean hidden;
    private boolean leavingPipe;

    public Mario(Point position) {
        movingSprites = new Hashtable<>(16);
        transitionSprites = new Hashtable<>(8);
        controller = new MarioController(this);
        instance = this;
        canMove = true;
        alive = true;
        currentAnimSpeed = ANIMATION_SPEED;
        convertSmall();
        updateSize();
        setLocation(position);
    }    

    private void convertSmall(){
        state = MarioState.SMALL;
        if(!transitioning){
            if (controller.getLastDirection() == RIGHT) {
                currentSprite = M_SMALL_RIGHT_IDLE;
            } else {
                currentSprite = M_SMALL_LEFT_IDLE;
            }
        }
        movingSprites.put("jump_r", M_SMALL_RIGHT_JUMP);
        movingSprites.put("jump_l", M_SMALL_LEFT_JUMP);
        movingSprites.put("walk1_r", M_SMALL_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_SMALL_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_SMALL_LEFT_WALK1);
        movingSprites.put("walk2_l", M_SMALL_LEFT_WALK2);
        movingSprites.put("idle_r", M_SMALL_RIGHT_IDLE);
        movingSprites.put("idle_l", M_SMALL_LEFT_IDLE);

        transitionSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_BIG_LEFT_IDLE);
        transitionSprites.put("trans_r",EMPTY);
        transitionSprites.put("trans_l", EMPTY);
    }

    private void convertBig(){
        state = MarioState.BIG;
        y-=Block.SIZE;
        if(controller.getLastDirection() == RIGHT){
            currentSprite = M_BIG_RIGHT_IDLE;
        }else{
            currentSprite = M_BIG_LEFT_IDLE;
        }
        
        movingSprites.put("jump_r", M_BIG_RIGHT_JUMP);
        movingSprites.put("jump_l", M_BIG_LEFT_JUMP);
        movingSprites.put("walk1_r", M_BIG_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_BIG_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_BIG_LEFT_WALK1);
        movingSprites.put("walk2_l", M_BIG_LEFT_WALK2);
        movingSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        movingSprites.put("idle_l", M_BIG_LEFT_IDLE);

        transitionSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_BIG_LEFT_IDLE);
        transitionSprites.put("trans_r", M_SMALL_RIGHT_TRANSITION);
        transitionSprites.put("trans_l", M_SMALL_LEFT_TRANSITION);
    }

    private void convertFire(){
        state = MarioState.FIRE;
        if (controller.getLastDirection() == RIGHT) {
            currentSprite = M_FIRE_RIGHT_IDLE;
        } else {
            currentSprite = M_FIRE_LEFT_IDLE;
        }
        movingSprites.put("jump_r", M_FIRE_RIGHT_JUMP);
        movingSprites.put("jump_l", M_FIRE_LEFT_JUMP);
        movingSprites.put("walk1_r", M_FIRE_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_FIRE_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_FIRE_LEFT_WALK1);
        movingSprites.put("walk2_l", M_FIRE_LEFT_WALK2);
        movingSprites.put("idle_r", M_FIRE_RIGHT_IDLE);
        movingSprites.put("idle_l", M_FIRE_LEFT_IDLE);

        transitionSprites.put("idle_r", M_FIRE_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_FIRE_LEFT_IDLE);
        transitionSprites.put("trans_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("trans_l", M_BIG_LEFT_IDLE);
    }

    private void updateSize() {
        int marioSpriteId;
        if (state.getSize() == 0) {
            marioSpriteId = M_SMALL_LEFT_IDLE;
        } else {
            marioSpriteId = M_BIG_LEFT_IDLE;
        }
        setColliderSize(Animator.getMarioSprite(marioSpriteId));
    }

    public void returnToState(MarioState state) {
        switch (state) {
        case SMALL:
            convertSmall();
            updateSize();
            break;
        case BIG:
            y -= Block.SIZE;
            convertBig();
            updateSize();
            break;
        case FIRE:
            y -= Block.SIZE;
            convertFire();
            updateSize();
            break;
        default:
            convertSmall();
            updateSize();
        }
    }
    
    public void tick() { 
        if(hidden){
            return;
        }

        if(levelFinished){
            checkFinishState();
            return;
        }
        
        if(alive){
            if (canMove) {
                controller.tick();
                isFalling = controller.isFalling();
                checkCollisions();
                controller.setCollisions(collisions);
            }
            if (invincible && !transitioning) {
                checkIfStillInvincible();
            }

            if(walking){
                walkEndlessly();
                if(walkingCollisions){
                    checkCollisions();
                }
                return;
            }

            if(leavingPipe){
                moveUntilPipe();
            }

            controller.moveCamera();
        }else{
            killProcess();
        }
                  
        animSprite();
        
    }

    private void checkIfStillInvincible(){
        if (invincibleCounter > INVINCIBLE_TICKS) {
            invincible = false;
        }
        invincibleCounter++;
    }

    private void killProcess(){
        if(deadCounter++ > DEAD_TICKS ){
            Score.getInstance().decreaseLives();
            GameRunner.instance.restartCurrentLevel();
        }
        killAnimation();
        deadCounter++;
    }

    private void killAnimation(){
        if(deadCounter < 120/4){
            verticalVelocity = -PhysicObject.getGravity();
        }else if(deadCounter < 120/2){
            verticalVelocity = -PhysicObject.getGravity()/2;
        }else if(deadCounter < 120/4 * 2){
            verticalVelocity = PhysicObject.getGravity()/2;
        }else{
            verticalVelocity = PhysicObject.getGravity();
        }

        setLocation(x, y + verticalVelocity);
    }

    private void walkEndlessly(){
        controller.walkEndlessly();
        if (animationCounter > currentAnimSpeed){
            if (currentSprite == movingSprites.get("walk1_r")) {
                currentSprite = movingSprites.get("walk2_r");
            } else {
                currentSprite = movingSprites.get("walk1_r");
            }
            animationCounter = 0;
        }
        animationCounter++;
    }


    private void moveUntilPipe(){
        if(y + height > lastPipeYPosition){
            int speed = (state.getSize() == MarioState.BIG.getSize()) ? 2 : 1;
            setLocation(x, y - speed);
            return;
        }

        leavingPipe = false;
        resetControls();
    }

    private void animSprite() {
        if (animationCounter > currentAnimSpeed) {
            if(!alive){
                currentSprite = M_DEAD;
            }else if(transitioning){
                updateTransSprite();
            }else{
                updateAnimSprite();
            }
            animationCounter = 0;
        }
        animationCounter++;
    }

    private void updateAnimSprite(){
        if(invincible){
            if (invincibleCounter % 2 == 0) {
                previousSprite = currentSprite;
                currentSprite = EMPTY;
                return;
            }else if((invincibleCounter-1)%2 == 0){
                currentSprite = previousSprite;
            }
        }

        if (controller.isJumping()) {
            if (controller.getLastDirection() == RIGHT) {
                currentSprite = movingSprites.get("jump_r");
            }else{
                currentSprite = movingSprites.get("jump_l");
            }
        }else

        if (controller.isMovingRight()) {
            if (currentSprite == movingSprites.get("walk1_r")) {
                currentSprite = movingSprites.get("walk2_r");
            }else{
                currentSprite = movingSprites.get("walk1_r");
            }
        }else

        if (controller.isMovingLeft()) {
            if (currentSprite == movingSprites.get("walk1_l")) {
                currentSprite = movingSprites.get("walk2_l");
            }else{
               currentSprite = movingSprites.get("walk1_l");
            }
        }else

        if (controller.getLastDirection() == RIGHT) {
            currentSprite = movingSprites.get("idle_r");
        }else{
            currentSprite = movingSprites.get("idle_l");
        }
    }

    private void updateTransSprite(){
        String direction = (controller.getLastDirection() == RIGHT) ? "r" : "l";

        if (currentSprite == transitionSprites.get("idle_"+direction)) {
            currentSprite = transitionSprites.get("trans_"+direction);
            return;
        }
        currentSprite = transitionSprites.get("idle_"+direction);

        transitionCounter++;
        if (transitionCounter >= 3) {
            resetControls();       
            currentSprite = movingSprites.get("idle_"+direction);
            if (state == MarioState.SMALL)
                y += Block.SIZE;
            if(invincible){
                startInvincibility();
            }
            return;
        }
    }

    private void checkFinishState(){
        int offset = (state.getSize() == MarioState.BIG.getSize()) ? Block.SIZE : 0;

        //Wait until flag and mario reach the ground
        if (finishFlag.y < 10 * Block.SIZE) {
            finishFlag.setLocation(finishFlag.x, finishFlag.y + 3);
            if (y  + offset < 10 * Block.SIZE) {
                setLocation(finishFlag.x + Block.SIZE - width, y + 3);
            }
            if (state == MarioState.BIG) {
                currentSprite = Animator.M_BIG_RIGHT_FLAG;
            } else if (state == MarioState.SMALL){
                currentSprite = Animator.M_SMALL_RIGHT_FLAG;
            }else{
                currentSprite = Animator.M_FIRE_RIGHT_FLAG;
            }
            currentAnimSpeed = 8;
            animationCounter = 0;
            return;
        }

        //Jump from the pole
        if(x < finishFlag.x + 1.5 * Block.SIZE){
            setLocation(x + Math.abs(controller.walkSpeed), y);
            if (y + Block.SIZE > finishFlag.y - offset) {
                setLocation(x, y - controller.jumpSpeed);
            }
            if (state == MarioState.BIG) {
                currentSprite = Animator.M_BIG_RIGHT_JUMP;
            } else if (state == MarioState.SMALL) {
                currentSprite = Animator.M_SMALL_RIGHT_JUMP;
            } else {
                currentSprite = Animator.M_FIRE_RIGHT_JUMP;
            }
            return;
        }else if(x < finishFlag.x + 3 * Block.SIZE){
            setLocation(x + Math.abs(controller.walkSpeed), y);
            if (y < finishFlag.y +Block.SIZE - offset) {
                setLocation(x, y + controller.jumpSpeed);
            }
            if (state == MarioState.BIG) {
                currentSprite = Animator.M_BIG_RIGHT_JUMP;
            } else if (state == MarioState.SMALL) {
                currentSprite = Animator.M_SMALL_RIGHT_JUMP;
            } else {
                currentSprite = Animator.M_FIRE_RIGHT_JUMP;
            }
            return;
        }

        //Walk towards end. It will reach a finish block        
        setLocation(x + controller.walkSpeed/2, y);
        if (animationCounter > currentAnimSpeed){
            if (currentSprite == movingSprites.get("walk1_r")) {
                currentSprite = movingSprites.get("walk2_r");
            } else {
                currentSprite = movingSprites.get("walk1_r");
            }
            animationCounter = 0;
        }     
        animationCounter++;   
    }

    private void startInvincibility(){
        invincibleCounter = 0;
        invincible = true;
    }

    public void resetControls(){
        hidden = false;
        walking = false;
        transitioning = false;
        canMove = true;
        currentAnimSpeed = ANIMATION_SPEED;
    }
    
    public void paintMario(Graphics g) {
        if(hidden){
            return;
        }
        super.paint(g);
        // g.setColor(Color.GREEN);
        // g.drawRect(x, y, width, height);
        paintSprite(g);
    }

    private void paintSprite(Graphics g) {
        g.drawImage(Animator.getMarioSprite(currentSprite), x, y, null);
    }

    public void applyMooshroom(){
        Sound.makeSound(Sound.POWER_UP);
        startChangeStateAnimation();
        convertBig();
        updateSize();
    }

    public void applyFire(){
        Sound.makeSound(Sound.POWER_UP);
        startChangeStateAnimation();
        convertFire();
        updateSize();
    }

    public void applyDamage(){
        if(invincible){
            return;
        }

        if(state == MarioState.SMALL){
            killMario();
        }else if(state == MarioState.BIG || state == MarioState.FIRE){
            Sound.makeSound(Sound.POWER_DOWN);
            startChangeStateAnimation();
            // Override the startChangeStateAnimation currentAnimSpeed
            currentAnimSpeed = (int) (ANIMATION_SPEED * 1.5);
            convertSmall();
            updateSize();
            startInvincibility();
        }
    }

    public void killMario(){
        if(!alive){
            return;
        }

        if(levelFinished){
            return;
        }

        canMove = false;
        invincible = false;
        alive = false;
        deadCounter = 0;
        Sound.stopAllSounds();
        Sound.makeSound(Sound.MARIO_DIE);
        GameRunner.instance.stopTimer();
        
    }

    private void startChangeStateAnimation(){
        canMove = false;
        transitioning = true;
        transitionCounter = 0;
        animationCounter = 0;
        currentAnimSpeed = (int) (ANIMATION_SPEED * 1.75);
    }

    public void startEndAnimation(PickUp flag){
        levelFinished = true;
        finishFlag = flag;
        animationCounter = -1;
    }

    public void startWalkAnimation(){
        canMove = false;
        walking = true;
        walkingCollisions = true;
        animationCounter = 0;
    }

    public void startWalkAnimationNoCollisions() {
        canMove = false;
        walking = true;
        walkingCollisions = false;
        animationCounter = 0;
    }

    public void exitPipe(int pipeYPosition){
        walking = false;
        leavingPipe = true;
        lastPipeYPosition = pipeYPosition;
    }

    public void activateMiniJump(){
        controller.activateMiniJump();
    }

    public void hide(){
        hidden = true;
    }

    public void showAsIdle(){
        currentSprite = movingSprites.get("idle_r");
    }

    public void keyPressed(int k) {
        if(canMove){
            controller.keyPressed(k);
        }
    }

    public void keyReleased(int k) {
        controller.keyReleased(k);
    }

    public static Mario getCurrentInstance(){
        return instance;
    }

    public static MarioState getCurrentState(){
        return instance.state;
    }

    public boolean isTransitioning(){
        return transitioning;
    }
    
    public boolean isAlive(){
        return alive;
    }

    public boolean hasFinishLevel(){
        return levelFinished;
    }

    public enum MarioState {
        SMALL(0), BIG(1), FIRE(1);

        private int size;

        private MarioState(int identifier) {
            size = identifier;
        }

        public int getSize() {
            return size;
        }
    }
}


