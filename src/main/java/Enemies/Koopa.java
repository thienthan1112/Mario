package main.java.Enemies;

import java.awt.Point;

import main.java.Animator;
import main.java.Block;
import main.java.PhysicObject;

public class Koopa extends Enemy  {

    public Koopa(Point position, int id) {
        super(position, id);
        sprite = Animator.K_NORMAL_LEFT_WALK1;
        horizontalVelocity = -KOOPA_VEL;
        verticalVelocity = PhysicObject.getGravity();
        hCollisionOffset = -horizontalVelocity;
        vCollisionOffset = verticalVelocity;
        tableSprites.put("right1", Animator.K_NORMAL_RIGHT_WALK1);
        tableSprites.put("right2", Animator.K_NORMAL_RIGHT_WALK2);
        tableSprites.put("left1", Animator.K_NORMAL_LEFT_WALK1);
        tableSprites.put("left2", Animator.K_NORMAL_LEFT_WALK2);
        tableSprites.put("dead", Animator.K_SHELL_NORMAL);
        tableSprites.put("flip", Animator.K_NORMAL_FLIP);
        setColliderSize(Animator.getEnemySprite(Animator.K_NORMAL_RIGHT_WALK1));
        setLocation(position.x, position.y-Block.SIZE/4);
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