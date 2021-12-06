//fire from boss
package main.java;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import main.java.Enemies.Enemy;

public class Firestrip extends Fireball{

    private static final int SPIN_SPEED = 8;

    private AffineTransform affineCollider;
    private AffineTransform affineSprite;
    private Shape outline;

    public Firestrip(Point position) {
        id = STRIP_FIRE;
        mario = Mario.getCurrentInstance();
        currentSprite = Animator.FIREBALL_STRIP;
        active = false;
        setLocation((int)(position.x + Block.SIZE * 0.375), (int)(position.y + Block.SIZE * 0.375));
        setColliderSize(Animator.getFireballSprite(Animator.FIREBALL_STRIP));
        initRotationVariables();
    }

    private void initRotationVariables(){
        affineCollider = AffineTransform.getTranslateInstance(0, 0);
        affineSprite = AffineTransform.getTranslateInstance(x, y);
        outline = affineCollider.createTransformedShape(this);
    }

    @Override
    public void tick(ArrayList<Enemy> enemies){
        if (!active) {
            checkForActivation();
            return;
        }

        if (mario.isTransitioning() || !mario.isAlive()) {
            return;
        }

        spin();
        checkMarioCollisions();
    }

    @Override
    public void paintFireball(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(Animator.getFireballSprite(currentSprite), affineSprite, GameRunner.instance);
    }

    private void spin(){
        affineCollider.rotate(Math.toRadians(1.5), x + SPIN_SPEED, y + SPIN_SPEED);
        affineSprite.rotate(Math.toRadians(1.5), SPIN_SPEED, SPIN_SPEED);
        outline = affineCollider.createTransformedShape(getBounds());
    }
    
    @Override
    protected void checkMarioCollisions(){
        if(outline.intersects(mario)){
            mario.applyDamage();
        }
    }

    public static boolean mustSpawnFireStrip(String[] token){
        if(token.length == 2){
            return token[0].equals("9") && token[1].equals("1");
        }
        return false;
    }


    
}
