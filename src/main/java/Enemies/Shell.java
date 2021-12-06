package main.java.Enemies;

import java.awt.Point;

import main.java.Animator;
import main.java.PhysicObject;
import main.java.Sound;

public class Shell extends Enemy {

    public Shell(Point position, int id) {
        super(position, id);
        sprite = Animator.K_SHELL_NORMAL;
        horizontalVelocity = 0;
        verticalVelocity = 0;
        hCollisionOffset = SHELL_VEL;
        vCollisionOffset = PhysicObject.getGravity();
        tableSprites.put("right1", Animator.K_SHELL_NORMAL);
        tableSprites.put("right2", Animator.K_SHELL_NORMAL);
        tableSprites.put("left1", Animator.K_SHELL_NORMAL);
        tableSprites.put("left2", Animator.K_SHELL_NORMAL);
        tableSprites.put("dead", Animator.K_SHELL_NORMAL);
        tableSprites.put("flip", Animator.K_SHELL_FLIP);
        setColliderSize(Animator.getEnemySprite(Animator.K_SHELL_NORMAL));
        setLocation(position.x, position.y);
    }

    public void tickEnemy(){
        isFalling = verticalVelocity > 0;
        applyVelocities();
        checkCollisions();
        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            Sound.makeSound(Sound.KICK);
        }
        checkMarioCollisions();
    }
    
}
