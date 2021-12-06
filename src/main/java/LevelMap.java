package main.java;

import static main.java.MarioController.LEFT;

import main.java.Enemies.Enemy;

import java.awt.*;
import java.io.*;
import java.util.*;



public class LevelMap {

    private static LevelMap instance;
    private Queue<Object> toRemove;
    private Queue<Object> toAdd;
    private Object auxObject;
    private int mapId;
    private Block[][] blocks;
    private ArrayList<BlockInteractable> interactableBlocks;
    private ArrayList<PickUp> pickUps;
    private ArrayList<Enemy> enemies;
    private ArrayList<Fireball> fireballs;
    
    public LevelMap(int mapId) {
        this.mapId = mapId;
        instance = this;
        toAdd = new LinkedList<>();
        toRemove = new LinkedList<>();
        interactableBlocks = new ArrayList<>();
        pickUps = new ArrayList<>();
        enemies = new ArrayList<>();
        fireballs = new ArrayList<>();
        loadMapFile();
    }

    private void loadMapFile() {
        try {
            BufferedReader fileBuffer = openLevelLayoutFile();
            readLevelLayoutFile(fileBuffer);
            fileBuffer.close();
            setPhysicsBlocks();
        } catch ( IOException e) {
            String message = "The file layout for level " + mapId + "could not be read.";
            MyLogger.logErrorMessage(message, e);
        }
    }

    private BufferedReader openLevelLayoutFile() throws FileNotFoundException {
        String mapFilePath = "res/levels/lvl" + mapId + ".ly";
        File mapFile = new File(mapFilePath);
        FileReader mapFileReader = new FileReader(mapFile);
        BufferedReader br = new BufferedReader(mapFileReader);
        return br;
    }

    private void readLevelLayoutFile(BufferedReader fileBuffer) throws NumberFormatException, IOException {
        int width = Integer.parseInt(fileBuffer.readLine());
        int height = Integer.parseInt(fileBuffer.readLine());
        blocks = new Block[height][width];

        String fileLine;
        String[] currentToken;
        StringTokenizer stringTokenizer;
        Point newBlockPosition;
        int newBlockId;
        int pickUpType;
        int enemyType;
        for (int i = 0; i < height; i++) {
            fileLine = fileBuffer.readLine();
            stringTokenizer = new StringTokenizer(fileLine);
            for (int j = 0; j < width; j++) {
                newBlockPosition = new Point(j*Block.SIZE,i*Block.SIZE);
                currentToken = stringTokenizer.nextToken().split("\\.");
                newBlockId = Integer.parseInt(currentToken[0]);

                if(Enemy.mustSpawnEnemy(currentToken)){
                    enemyType = Integer.parseInt(currentToken[1]);
                    if(enemyType == Enemy.getFireId()){
                        fireballs.add(new Fireball(newBlockPosition, LEFT, Fireball.ENEMY_FIRE));
                    }else{
                        enemies.add(Enemy.create(newBlockPosition, enemyType));
                    }
                    blocks[i][j] = new Block(newBlockPosition, newBlockId);
                    continue;
                }

                if(Firestrip.mustSpawnFireStrip(currentToken)){
                    fireballs.add(new Firestrip(newBlockPosition));
                    blocks[i][j] = new Block(newBlockPosition, newBlockId);
                    continue;
                }

                if(BlockInteractable.mustBeInteractable(newBlockId)){
                    if(currentToken.length > 1){
                        pickUpType = Integer.parseInt(currentToken[1]);
                        if(PickUp.mustReplaceBlock(pickUpType)){
                            pickUps.add(new PickUp(PickUp.Type.typeById(pickUpType), newBlockPosition));
                            blocks[i][j] = new Block(newBlockPosition, 0);
                            continue;
                        }else{
                            blocks[i][j] = new BlockInteractable(newBlockPosition, newBlockId,PickUp.Type.typeById(pickUpType));
                            pickUps.add(BlockInteractable.getPickUp((BlockInteractable) blocks[i][j]));
                        }
                    }else{
                        blocks[i][j] = new BlockInteractable(newBlockPosition, newBlockId);
                    }
                    interactableBlocks.add((BlockInteractable) blocks[i][j]);
                }else {
                    blocks[i][j] = new Block(newBlockPosition, newBlockId);
                }
                
            }
        }
    }

    public void turnOnDarkMode(){
        for(Block[] array : blocks){
            for(Block b : array){
                b.darkMode();
            }
        }
    }

    public void turnOffDarkMode(){
        for (Block[] array : blocks) {
            for (Block b : array) {
                b.lightMode();
            }
        }
    }

    private void setPhysicsBlocks(){
        PhysicObject.cleanMapBlocks();
        for(Block[] blockArray : blocks){
            for(Block block : blockArray){
                if(block.getId()!=0){
                    PhysicObject.addMapBlock(block);
                }
            }
        }
    }

    public void paintBlocks(Graphics g, int marioXPos){
        for(Block[] arrayB: blocks){
            for(Block b : arrayB){
                if(b.getId() == Block.GRASS_SUPPORT){ //This blocks need to be painted behind everything.
                    continue;
                }
                if(b.x > marioXPos-Block.SIZE - WindowManager.windowWidth &&
                   b.x < marioXPos+Block.SIZE + WindowManager.windowWidth){
                    b.paintBlock(g);
                }
            }
        }
    }

    public void paintPickUps(Graphics g){
        try {
            for (PickUp p : pickUps) {
                p.paintPickUp(g);
            }
        } catch (ConcurrentModificationException ex) {
            // The pickUp list was modified, but swing thread is behind.
        }
        
    }

    public void paintEnemies(Graphics g){
        try {
            for (Enemy e : enemies) {
                e.paintEnemy(g);
            }
        } catch (ConcurrentModificationException ex) {
            // The enemy list was modified, but swing thread is behind.
        }
        
    }

    public void paintFireballs(Graphics g){
        try {
            for (Fireball f : fireballs) {
                f.paintFireball(g);
            }
        } catch (ConcurrentModificationException ex) {
            // The fireball list was modified, but swing thread is behind.
        }
        
    }

    public void tickInteractableBlocks(){
        for(BlockInteractable b : interactableBlocks){
            b.tick();
        }
    }

    public void tickPickUps(){
        for(PickUp p : pickUps){
            p.tick();
        }
    }

    public void tickEnemies(){
        for(Enemy e : enemies){
            e.tick(enemies);
        }
    }

    public void tickFireballs(){
        for(Fireball f : fireballs){
            f.tick(enemies);
        }
    }

    public void removeUsedObjects(){
        while(!toRemove.isEmpty()){
            auxObject = toRemove.poll();
            if (auxObject instanceof Enemy) {
                enemies.remove(auxObject);
            } else if (auxObject instanceof PickUp) {
                pickUps.remove(auxObject);
            }else if(auxObject instanceof Fireball){
                fireballs.remove(auxObject);
            }
        }
    }
    
    public void addNewObjects(){
        while (!toAdd.isEmpty()) {
            auxObject = toAdd.poll();
            if (auxObject instanceof Enemy) {
                enemies.add((Enemy)auxObject);
            } else if (auxObject instanceof Fireball) {
                fireballs.add((Fireball)auxObject);
            } else if(auxObject instanceof PickUp){
                pickUps.add((PickUp) auxObject);
            }
        }
    }

    public PickUp getFlag() throws NoSuchElementException{
        for(PickUp p : pickUps){
            if(p.getId() == PickUp.getFlagId()){
                return p;
            }
        }
        throw new NoSuchElementException();
    }

    public Enemy getBowser() throws NoSuchElementException{
        for(Enemy e : enemies){
            if(e.getId() == Enemy.getBowserId()){
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    public Block[][] getBlocks(){
        return blocks;
    }

    public static void addObject(Object o){
        instance.toAdd.add(o);
    }

    public static void deleteObject(Object obj){
        instance.toRemove.add(obj);
    }

}
