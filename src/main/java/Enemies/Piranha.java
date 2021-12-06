package main.java.Enemies;

import java.awt.Point;

import main.java.Animator;
import main.java.Block;

public class Piranha extends Enemy {

    public Piranha(Point position, int id) {
        super(position, id);
        sprite = Animator.PI_CLOSE;
        horizontalVelocity = 0;
        verticalVelocity = -PIRANHA_VEL;
        tableSprites.put("right1", Animator.PI_CLOSE);
        tableSprites.put("right2", Animator.PI_OPEN);
        tableSprites.put("left1", Animator.PI_CLOSE);
        tableSprites.put("left2", Animator.PI_OPEN);
        tableSprites.put("dead", Animator.G_RIGHT);
        tableSprites.put("flip", Animator.G_LEFT);
        setColliderSize(Animator.getEnemySprite(Animator.PI_OPEN));
        setLocation(position.x+Block.SIZE/2 + (Block.SIZE - width)/2, position.y + Block.SIZE);
    }

    @Override
    public void tickEnemy(){
        if (behaviorCounter <= 150) {
            verticalVelocity = 0;
        } else if (behaviorCounter <= 151 + height) {
            verticalVelocity = -1;
        } else if (behaviorCounter <= 151 + height * 2) {
            verticalVelocity = 1;
        } else {
            behaviorCounter = 0;
        }
        behaviorCounter++;
        changeSpriteDirection();
        setLocation(x, y + verticalVelocity);
        checkMarioCollisions();
    }
    
}
