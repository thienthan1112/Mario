package main.java.Enemies;

import static main.java.MarioController.LEFT;

import java.awt.Point;

import main.java.Animator;
import main.java.Block;
import main.java.Fireball;
import main.java.LevelMap;
import main.java.PhysicObject;

public class Bowser extends Enemy {

    public Bowser(Point position, int id) {
        super(position, id);
        sprite = Animator.BOWSER1;
        horizontalVelocity = -BOWSER_VEL;
        verticalVelocity = BOWSER_VEL * 2;
        hCollisionOffset = BOWSER_VEL;
        vCollisionOffset = verticalVelocity;
        tableSprites.put("right1", Animator.BOWSER1);
        tableSprites.put("right2", Animator.BOWSER2);
        tableSprites.put("left1", Animator.BOWSER3);
        tableSprites.put("left2", Animator.BOWSER4);
        tableSprites.put("dead", Animator.BOWSER1);
        tableSprites.put("flip", Animator.BOWSER1);
        setColliderSize(Animator.getEnemySprite(Animator.BOWSER1));
        setLocation(position.x, position.y-Block.SIZE);
    }

    public void tickEnemy(){
        checkCollisions();
        movement();
        changeSprite();

        if (intersects(mario)) {
            mario.applyDamage();
        }
    }

    private void movement() {
        // Move horizontally
        if (behaviorCounter > 150) {
            horizontalVelocity *= -1;
            behaviorCounter = 0;
        }

        // Check for jumptime
        if (jumpCounter == 40) {
            verticalVelocity = Math.abs(verticalVelocity);
        } else if (jumpCounter >= 40) {
            jumpCounter = 0;
        }

        // Activate jump (if possible)
        if (behaviorCounter == 50) {
            if (collisions[PhysicObject.COLLISION_BOTTOM]) {
                verticalVelocity = -Math.abs(verticalVelocity);
                jumpCounter = 0;
            }
        }

        // Throw fire
        if (behaviorCounter % 110 == 0) {
            Fireball ball = new Fireball(new Point(x - Block.SIZE, y + Block.SIZE / 4), LEFT, Fireball.ENEMY_FIRE);
            LevelMap.addObject(ball);
        }

        behaviorCounter++;
        jumpCounter++;
        setLocation(x + horizontalVelocity, y + verticalVelocity);
    }

    private void changeSprite() {
        if (animCounter < Animator.ANIMATION_SPEED) {
            sprite = tableSprites.get("right1");
        } else if (animCounter < Animator.ANIMATION_SPEED * 2) {
            sprite = tableSprites.get("right2");
        } else if (animCounter < Animator.ANIMATION_SPEED * 3) {
            sprite = tableSprites.get("left1");
        } else if (animCounter < Animator.ANIMATION_SPEED * 4) {
            sprite = tableSprites.get("left2");
        } else {
            animCounter = 0;
        }
        animCounter++;

    }
}
