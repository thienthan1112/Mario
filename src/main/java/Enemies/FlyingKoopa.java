package main.java.Enemies;

import java.awt.Point;

import main.java.Animator;
import main.java.Block;

public class FlyingKoopa extends Enemy {

    public FlyingKoopa(Point position, int id) {
        super(position, id);
        sprite = Animator.K_FLY_LEFT_WALK1;
        horizontalVelocity = 0;
        verticalVelocity = -KOOPA_FLYING_VEL;
        hCollisionOffset = 2;
        vCollisionOffset = -verticalVelocity;
        tableSprites.put("right1", Animator.K_FLY_RIGHT_WALK1);
        tableSprites.put("right2", Animator.K_FLY_RIGHT_WALK2);
        tableSprites.put("left1", Animator.K_FLY_LEFT_WALK1);
        tableSprites.put("left2", Animator.K_FLY_LEFT_WALK2);
        tableSprites.put("dead", Animator.K_NORMAL_RIGHT_WALK1);
        tableSprites.put("flip", Animator.K_NORMAL_FLIP);
        setColliderSize(Animator.getEnemySprite(Animator.K_FLY_RIGHT_WALK1));
        setLocation(position.x, position.y + Block.SIZE);
    }

    @Override
    public void tickEnemy(){
        applyVelocities();
        changeSpriteDirection();
        checkCollisions();
        checkMarioCollisions();
    }

    @Override
    public void applyVelocities(){
        if (behaviorCounter <= 80) {
            verticalVelocity = -KOOPA_FLYING_VEL;
        } else if (behaviorCounter <= 159) {
            verticalVelocity = KOOPA_FLYING_VEL;
        } else {
            behaviorCounter = 0;
        }
        behaviorCounter++;

        setLocation(x + horizontalVelocity, y + verticalVelocity);
    }
    
}
