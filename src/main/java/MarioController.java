package main.java;

import java.awt.Point;
import java.awt.event.KeyEvent;

import main.java.Mario.MarioState;

public class MarioController {
    public static final boolean RIGHT = true;
    public static final boolean LEFT = false;

    private final int maxJumpTime = 34;
    public final int walkSpeed = 5;
    public final int jumpSpeed = 7;
    private final int gravity = PhysicObject.getGravity();
    

    private GameRunner gameRunner;
    private Mario mario;
    
    private boolean[] collisions;
    private int jumpTime;
    private int fireballCooldown;
    private boolean moveRight;
    private boolean moveLeft;
    private boolean jumping;
    private boolean lastDirection;
    

    public MarioController(Mario mario){
        this.mario = mario;
        mario.hCollisionOffset = walkSpeed;
        mario.vCollisionOffset = gravity;
        lastDirection = RIGHT;
        collisions = new boolean[4];
        gameRunner = GameRunner.instance;
    }

    public void keyPressed(int k){
        if (k == KeyEvent.VK_UP) {
            upKeyPressed();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyPressed();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyPressed();
        } else if (k == KeyEvent.VK_X){
            powerKeyPressed();
        }
    }

    private void upKeyPressed() {
        if(collisions[PhysicObject.COLLISION_BOTTOM]) {
            activateJump();
        }
    }

    private void activateJump(){
        Sound.makeSound(Sound.JUMP);
        jumpTime = 0;
        jumping = true;
        mario.canActivateBlocks = true;
        mario.setLocation(mario.x, mario.y-jumpSpeed);
    }

    private void rightKeyPressed() {
        moveRight = true;
        lastDirection = RIGHT;
    }

    private void leftKeyPressed() {
        moveLeft = true;
        lastDirection = LEFT;
    }

    private void powerKeyPressed(){
        if(mario.state != MarioState.FIRE || fireballCooldown != 0){
            return;
        }
        spawnFireball();
    }

    private void spawnFireball(){
        fireballCooldown = 10;
        Point firePosition = mario.getLocation();
        firePosition.y += Block.SIZE;
        firePosition.x += mario.getWidth()/2;
        LevelMap.addObject(new Fireball(firePosition, lastDirection, Fireball.MARIO_FIRE));
    }

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_UP) {
            upKeyReleased();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyReleased();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyReleased();
        }
    }

    private void upKeyReleased() {
        deactivateJump();
        mario.canActivateBlocks = false;
    }

    private void deactivateJump() {
        jumping = false;
    }

    private void leftKeyReleased() {
        moveLeft = false;
    }

    private void rightKeyReleased() {
        moveRight = false;
    }

    public void tick(){
        findCurrentAction();
        if(jumping)
            jump();
        
        applyVelocities();
        if(fireballCooldown > 0){
            fireballCooldown--;
        }
    }

    public void jump(){
        if(jumpTime > maxJumpTime){
            deactivateJump();
            mario.canActivateBlocks = false;
        }
        jumpTime++;
    }

    public void activateMiniJump(){
        jumpTime = maxJumpTime/2;
        jumping = true;
        mario.canActivateBlocks = true;
        mario.setLocation(mario.x, mario.y - jumpSpeed);
    }

    private void findCurrentAction() {
        if(jumping){
            if(collisions[PhysicObject.COLLISION_TOP]){
                deactivateJump();
                mario.verticalVelocity = gravity;
            }else{
                mario.verticalVelocity = -jumpSpeed;
            }
        }else if(collisions[PhysicObject.COLLISION_BOTTOM]){
            mario.verticalVelocity = 0;
        }else{
            mario.verticalVelocity = gravity;
        }
        
    }

    private void applyVelocities(){
        if (moveRight)
            mario.horizontalVelocity = walkSpeed;
        else if (moveLeft)
            mario.horizontalVelocity = -walkSpeed;
        else
            mario.horizontalVelocity = 0;

        mario.setLocation(mario.x + mario.horizontalVelocity, mario.y + mario.verticalVelocity);
    }

    public void walkEndlessly(){
        findCurrentAction();
        mario.horizontalVelocity = walkSpeed;
        mario.setLocation(mario.x + mario.horizontalVelocity, mario.y + mario.verticalVelocity);
    }

    public void moveCamera(){
        gameRunner.moveHorizontalScroll(mario.x);
    }

    public boolean isMovingRight(){
        return moveRight;
    }

    public boolean isMovingLeft(){
        return moveLeft;
    }

    public boolean isJumping(){
        return jumping;
    }

    public boolean isFalling(){
        return mario.verticalVelocity>0;
    }

    public boolean getLastDirection(){
        return lastDirection;
    }

    public static int getWalkSpeed(){
        return 5;
    }

    public void setCollisions(boolean[] collisions){
        this.collisions = collisions;
    }



    

    
}
