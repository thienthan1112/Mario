package main.java;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.java.Enemies.Enemy;

public class PhysicObject extends Rectangle {

    public static final int COLLISION_TOP = 0;
    public static final int COLLISION_RIGHT = 1;
    public static final int COLLISION_BOTTOM = 2;
    public static final int COLLISION_LEFT = 3;

    private static final long serialVersionUID = 1L;
    private static final int GRAVITY = 7;
    private static final int collisionOffset = 0;

    private static ArrayList<Block> mapBlocks = new ArrayList<>();

    protected boolean[] collisions = new boolean[4];

    private Rectangle tCollider;
    private Rectangle rCollider;
    private Rectangle bCollider;
    private Rectangle lCollider;

    public int hCollisionOffset;
    public int vCollisionOffset;
    public int horizontalVelocity;
    public int verticalVelocity;

    public boolean isFalling;
    public boolean canActivateBlocks;

    protected void checkCollisions() {
        setTopCollider();
        setRightCollider();
        setBottomCollider();
        setLeftCollider();

        boolean tCollision = false;
        boolean rCollision = false;
        boolean bCollision = false;
        boolean lCollision = false;

        for (Block block : mapBlocks) {
            if (block.isActive()) {
                if(block.getId() == Block.ENEMY_AI && ( this instanceof Mario || Enemy.isShell(this) || this instanceof Fireball)){
                    continue;
                }

                if (!isFalling && tCollider.intersects(block)) {
                    setLocation(x, block.y + Block.SIZE + collisionOffset);
                    tCollision = true;
                    if (canActivateBlocks && block instanceof BlockInteractable) {
                        block.activateBlock();
                    }
                }

                if (rCollider.intersects(block)) {
                    setLocation(block.x - (int) getWidth() - collisionOffset, y);
                    rCollision = true;
                    if (block.getId() == Block.FLAG_POST && this instanceof Mario) {
                        block.activateBlock();
                    }
                }

                if (bCollider.intersects(block)) {
                    setLocation(x, block.y - (int) getHeight() - collisionOffset);
                    bCollision = true;
                }

                if (lCollider.intersects(block)) {
                    setLocation(block.x + Block.SIZE + collisionOffset, y);
                    lCollision = true;
                }

            }
        }

        collisions[COLLISION_TOP] = tCollision;
        collisions[COLLISION_RIGHT] = rCollision;
        collisions[COLLISION_BOTTOM] = bCollision;
        collisions[COLLISION_LEFT] = lCollision;
    }

    protected void applyVelocities() {
        if (collisions[PhysicObject.COLLISION_BOTTOM]) {
            verticalVelocity = 0;
        } else {
            verticalVelocity = PhysicObject.getGravity();
        }

        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            horizontalVelocity *= -1;
        }

        setLocation(x + horizontalVelocity, y + verticalVelocity);
    }

    protected void setColliderSize(BufferedImage sizeReference) {
        int width = sizeReference.getWidth();
        int height = sizeReference.getHeight();

        setSize(width, height);
    }

    private void setTopCollider() {
        Point coordinate = new Point(x + hCollisionOffset, y - vCollisionOffset);
        Dimension collisionSize = new Dimension((int) getWidth() - hCollisionOffset * 2, vCollisionOffset * 2);
        tCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setRightCollider() {
        Point coordinate = new Point(x + (int) getWidth() - hCollisionOffset, y + vCollisionOffset);
        Dimension collisionSize = new Dimension(hCollisionOffset * 2, (int) getHeight() - vCollisionOffset * 2);
        rCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setBottomCollider() {
        Point coordinate = new Point(x + hCollisionOffset, y + (int) getHeight() - vCollisionOffset);
        Dimension collisionSize = new Dimension((int) getWidth() - hCollisionOffset * 2, vCollisionOffset * 2);
        bCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setLeftCollider() {
        Point coordinate = new Point(x - hCollisionOffset, y + vCollisionOffset);
        Dimension collisionSize = new Dimension(hCollisionOffset * 2, (int) getHeight() - vCollisionOffset * 2);
        lCollider = new Rectangle(coordinate, collisionSize);
    }

    protected void paint(Graphics g) {
        //paintColliders(g);
    }

    @SuppressWarnings("unused")
    private void paintColliders(Graphics g) {
        g.setColor(Color.blue);
        setTopCollider();
        g.drawRect(tCollider.x, tCollider.y, tCollider.width, tCollider.height);

        g.setColor(Color.red);
        setRightCollider();
        g.drawRect(rCollider.x, rCollider.y, rCollider.width, rCollider.height);

        g.setColor(Color.green);
        setBottomCollider();
        g.drawRect(bCollider.x, bCollider.y, bCollider.width, bCollider.height);

        g.setColor(Color.black);
        setLeftCollider();
        g.drawRect(lCollider.x, lCollider.y, lCollider.width, lCollider.height);

    }

    public static void cleanMapBlocks(){
        mapBlocks = new ArrayList<>();
    }

    public static void addMapBlock(Block block) {
        mapBlocks.add(block);
    }

    public static int getGravity() {
        return GRAVITY;
    }
}