package app.initialization;

import app.interactables.Bullet;
import app.interactables.Enemy;
import app.interactables.Interactable;
import app.interactables.Key;
import app.interactables.Weapon;
import app.mechanics.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Map is one of the game-engines most important classes,
 * in it you can decide how the map is shaped, how large it is,
 * which enemies stand where, where the player starts and what objects are on the map.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class GameMap {
    private int size;
    private int[][] mapDesign;
    private Dimension blockSize;

    private List<Interactable> objects;
    private List<Enemy> enemies;
    private List<Weapon> weapons;
    private Key key;
    private Point startPos;
    private List<Bullet> bullets;
    private Dimension pixelSize;

    private Map<Integer, Block> blockMap;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    /**
     * The constructor for GameMap
     *
     * @param size 	the size of the map measured in blocks
     * @param mapDesign	the design of the map
     * @param blockSize	the size of the maps blocks
     * @param objects	a list of the objects on the map
     * @param enemies	a list of all enemies on the map
     * @param weapons	a list of all weapons on the map
     * @param key	the key which opens the maps final door
     * @param startPos	the starting position of the player
     */
    public GameMap(final int size, final int[][] mapDesign, final Dimension blockSize, final List<Interactable> objects,
		   final List<Enemy> enemies, final List<Weapon> weapons, final Key key, final Point startPos) {
	this.size = size;
	this.mapDesign = mapDesign;
	this.blockSize = blockSize;
	this.objects = objects;
	this.enemies = enemies;
	this.weapons = weapons;
	this.key = key;
	this.startPos = startPos;
	this.blockMap = new HashMap<>();
	this.bullets = new ArrayList<>();
	this.pixelSize = new Dimension(size*blockSize.width, size*blockSize.height);
	createBlockMap();
	LOGGER.info("Map initialized");
    }

    private void createBlockMap() {
	final int floorVal = 0;
	final int wallVal = 1;
	final int doorVal = 2;

	blockMap.put(floorVal, Block.FLOOR);
	blockMap.put(wallVal, Block.WALL);
	blockMap.put(doorVal, Block.DOOR);
    }


    public void draw(Graphics g) {
	g.setColor(Color.BLACK);
	double relativeBlockSize = blockSize.width/getScalingFactor();

	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		if (mapDesign[i][j] == 0)
		    continue;
		int posX = (int)Math.round(relativeBlockSize*j);
		int posY = (int)Math.round(relativeBlockSize*i);

		g.fillRect(posX, posY, (int)Math.round(relativeBlockSize), (int)Math.round(relativeBlockSize));
	    }
	}
    }

    public boolean pointOnMap(int x, int y) {
	if(x >= size || x < 0 || y >= size || y < 0) return false;
	return true;
    }


    public Dimension getBlockSize() {
	return blockSize;
    }

    public Interactable getObject(int i) {
	return objects.get(i);
    }
    public Enemy getEnemy(int i) { return enemies.get(i);}
    public Weapon getWeapon(int i) { return weapons.get(i); }

    public Key getKey() {
	return key;
    }

    public int getNumberOfObjects() {
	return objects.size();
    }

    public int getNumberOfWeapons() { return weapons.size(); }

    public int getNumberOfEnemies() {
	return enemies.size();
    }

    public Block getBlock(int x, int y) {
	if(x>size || y>size) return null;
	int value = this.mapDesign[y][x];
	return blockMap.get(value);
    }

    public int getSize() {
	return size;
    }

    public Point getStartPos() {
	return startPos;
    }

    public void addObject(Interactable interactable){
	objects.add(interactable);
    }

    public void removeInteractable(Interactable interactable) {
	enemies.remove(interactable);
	objects.remove(interactable);
    }

    public void addBullet(Bullet bullet) {
	this.bullets.add(bullet);
    }

    public void removeBullet(Bullet bullet) {
	this.bullets.remove(bullet);
    }

    public int getNumberOfBullets() {
	return bullets.size();
    }

    public Bullet getBullet(int i) {
	return bullets.get(i);
    }


    public double getScalingFactor() {
	return pixelSize.width/(double) Config.FRAME_WIDTH;
    }


    public void sortInteractables(Comparator<Interactable> comp) {
	objects.sort(comp);
    }
}