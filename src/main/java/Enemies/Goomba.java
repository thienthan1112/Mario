package main.java.Enemies;

import java.awt.Point;

import main.java.Animator;
import main.java.PhysicObject;

public class Goomba extends Enemy {

    public Goomba(Point position, int id) {
        super(position, id);
        sprite = Animator.G_LEFT;
        horizontalVelocity = -GOOMBA_VEL;
        verticalVelocity = PhysicObject.getGravity();
        hCollisionOffset = -horizontalVelocity;
        vCollisionOffset = verticalVelocity * 2;
        tableSprites.put("right1", Animator.G_RIGHT);
        tableSprites.put("right2", Animator.G_RIGHT);
        tableSprites.put("left1", Animator.G_LEFT);
        tableSprites.put("left2", Animator.G_LEFT);
        tableSprites.put("dead", Animator.G_SMASH);
        tableSprites.put("flip", Animator.G_FLIP);
        setColliderSize(Animator.getEnemySprite(Animator.G_RIGHT));
        setLocation(position);
    }

    @Override
    public void tickEnemy(){
        isFalling = verticalVelocity > 0;
        applyVelocities();
        changeSpriteDirection();
        checkCollisions();
        checkMarioCollisions();
    }
    
}
