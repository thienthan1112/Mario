package main.java;

import java.awt.Point;

import main.java.Mario.MarioState;

public class BlockInteractable extends Block {

    private static final long serialVersionUID = 1L;
    private static final int UP_SPEED = 2;
    private static final int MAX_COINS_TICKS = 300;
    private PickUp pickUp;
    private byte animationCounter;
    private int multipleCoinCounter;
    private boolean animating;
    private boolean broken;
    private boolean used;
    private boolean multipleCoin;
    private boolean spawnCoins;

    public BlockInteractable(Point position, int id) {
        super(position, id);
        collision = true;
    }

    public BlockInteractable(Point position, int id, PickUp.Type pType){
        this(position, id);
        this.pickUp = new PickUp(pType, position);
        multipleCoin = (pType == PickUp.Type.COIN_MULTIPLE) ? true : false;
    }

    @Override
    public void activateBlock() {
        if(used)
            return;

        if (spawnCoins) {
            checkForAnimation();
            if(!multipleCoin){
                used = true;
                currentSprite = Animator.getBlockSprite(Block.USED);
                dropPickUp();
            }else{
                dropCoin();
            }
            return;
        }

        switch(getId()){
        case Block.BREAKABLE:
            if(multipleCoin && !spawnCoins){
                spawnCoins = true;
                dropCoin();
                break;
            }

            dropPickUp();
            
            if(pickUp != null){
                currentSprite = SpriteAssets.getBlockSprite(Block.USED);
                used = true;
                break;
            }

            if(Mario.getCurrentState().getSize() == MarioState.BIG.getSize()){
                collision = false;
                broken = true;
                Sound.makeSound(Sound.BLOCK_BREAK);
                break;
            }
            Sound.makeSound(Sound.BLOCK_HIT);
            break;
        case Block.MISTERY:
            dropPickUp();
            deactivateBlock();
            used = true;
            Sound.makeSound(Sound.BLOCK_HIT);
            break;
        case Block.FLAG_POST:
            if(Mario.getCurrentInstance().y + Mario.getCurrentInstance().height >= Block.SIZE* 11){
                return;
            }
            GameRunner.instance.endCurrentLevel();
            return;
        }
        checkForAnimation();
    }

    private void checkForAnimation(){
        if(!animating){
            animating = true;
            animationCounter = 0;
        }
    }

    private void dropCoin(){
        PickUp coin = new PickUp(PickUp.Type.COIN, getLocation());
        LevelMap.addObject(coin);
        coin.spawnPickUp(new Point(x, y - Block.SIZE));
    }

    private void dropPickUp(){
        if(pickUp != null){
            Point p = new Point(x, y-Block.SIZE);
            pickUp.spawnPickUp(p);
        }
    }

    public void tick() {
        if (animating) {
            animateBlock();
        }

        if(spawnCoins){
            tickCoins();
        }
    }

    private void animateBlock() {
        if (broken) {
            breakBlock();
        }else{
            upAnimation();
        }
    }

    private void tickCoins(){
        if(multipleCoinCounter > MAX_COINS_TICKS){
            multipleCoin = false;            
        }
        multipleCoinCounter++;
    }

    private void breakBlock() {
        if (animationCounter < 10) {
            setLocation(x, y - UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM1);
        } else if (animationCounter < 20) {
            setLocation(x, y - UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM2);
        } else if (animationCounter < 30) {
            setLocation(x, y + UP_SPEED * 2);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM3);
        } else {
            deactivateBlock();
            broken = false;
        }
        animationCounter++;
    }

    private void upAnimation(){
        if (animationCounter < 8) {
            setLocation(x, y - UP_SPEED);
        } else if (animationCounter < 16) {
            setLocation(x, y + UP_SPEED);
        } else{
            animating = false;
        }
        animationCounter++;
    }

    public boolean shouldCollide() {
        return collision;
    }

    public PickUp getPickUp(){
        return pickUp;
    }

    public static boolean mustBeInteractable(int id) {
        return id == Block.BREAKABLE || id == Block.MISTERY || id == Block.FLAG_POST;
    }

    public static PickUp getPickUp(BlockInteractable block){
        return block.pickUp;
    }
}