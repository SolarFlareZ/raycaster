package app.initialization;

import app.interactables.Interactable;
import app.resourcehandlers.Jukebox;
import app.mechanics.Animate;
import app.mechanics.Block;
import app.mechanics.Direction;
import app.mechanics.Inventory;
import app.mechanics.Ray;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
* The player-class is used to create the player of the game.
* The class handles movement and vision of the player.
 * @author Oliver Björklund
 * @version 1.0
 */

public class Player implements Animate
{
    private double angle;
    private Point currentPos = null;
    private Ray[] rays;
    private Map<Direction, Boolean> directionMap;
    private GameMap gameMap = null;
    private int size;
    private Inventory inventory = null;
    private int hp;
    private int maxHp;
    private Jukebox hitSound;
    private Jukebox deathSound;
    private Game game;
    private boolean haskey;

    private static final Logger LOGGER = Logger.getLogger("GameLogger");



    public Player(final Game game, final GameMap gameMap) {
	final int numberOfRays = 121;
	this.directionMap = new EnumMap<>(Direction.class);
	this.rays = new Ray[numberOfRays];
	this.game = game;
	this.haskey = false;
	this.size = 16;
	this.maxHp = 100;
	this.hitSound = new Jukebox("audio/player_hit.wav");
	this.deathSound = new Jukebox("audio/player_death.wav");
	createDirectionMap();
	initPlayer(gameMap);
	LOGGER.config("Player successfully created");
    }

    private void createDirectionMap() {
	directionMap.put(Direction.RIGHT, false);
	directionMap.put(Direction.LEFT, false);
	directionMap.put(Direction.UP, false);
	directionMap.put(Direction.DOWN, false);
	directionMap.put(Direction.ROT_LEFT, false);
	directionMap.put(Direction.ROT_RIGHT, false);
    }

    public void initPlayer(GameMap map) {
	this.gameMap = map;
	this.currentPos = gameMap.getStartPos();

	this.angle = 0;
	this.hp = this.maxHp;
	this.inventory = new Inventory(Color.BLUE);
	this.haskey = false;
    }

    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D)g;
	double scalingFactor = gameMap.getScalingFactor();
	Point relativePos = new Point((int)Math.round(currentPos.x/scalingFactor),
				      (int)Math.round(currentPos.y/scalingFactor));

	int relativeSize = (int)Math.round(size / scalingFactor);

	int offset = (int)Math.round(size / scalingFactor / 2);

	g2d.setColor(Color.RED);
	g2d.rotate(angle, relativePos.x, relativePos.y);
	g2d.fillRect(relativePos.x-offset, relativePos.y-offset, relativeSize, relativeSize);
    }

    public void toggleMovement(Direction dir, boolean moving) {
	directionMap.replace(dir, moving);
    }

    public void moveTick() {
	final int speed = 5;
	final double rotationSpeed = 0.05;
	int posDiffCos = (int)(speed * Math.cos(angle));
	int posDiffSin = (int)(speed * Math.sin(angle));


	Point newPos = new Point(currentPos.x, currentPos.y);

	for (final Map.Entry<Direction, Boolean> entry : directionMap.entrySet()) {
	    if(!entry.getValue()) continue;
	    switch(entry.getKey()) {
		case LEFT -> {
		    newPos.x -= posDiffCos;
		    newPos.y -= posDiffSin;
		}
		case RIGHT ->  {
		    newPos.x += posDiffCos;
		    newPos.y += posDiffSin;
		}
		case UP ->  {
		    newPos.x += posDiffSin;
		    newPos.y -= posDiffCos;
		}
		case DOWN ->  {
		    newPos.x -= posDiffSin;
		    newPos.y += posDiffCos;
		}
		case ROT_LEFT -> {
		    double newAngle = angle-rotationSpeed;
		    angle = newAngle < 0 ? newAngle+2*Math.PI : newAngle;
		}
		case ROT_RIGHT ->  {
		    double newAngle = angle+rotationSpeed;
		    angle = newAngle > Math.PI*2 ? newAngle-2*Math.PI : newAngle;
		}
	    }
	}

	if (!boundaryControl(newPos)) currentPos = newPos;
    }

    private boolean boundaryControl(Point pos) {
	final int hitRadius = 5;

	for (int x = -hitRadius; x <= hitRadius; x+=hitRadius) {
	    for (int y = -hitRadius; y <= hitRadius; y+=hitRadius) {
		int mapX = (pos.x + x) / gameMap.getBlockSize().width;
		int mapY = (pos.y + y) / gameMap.getBlockSize().height;
		if(gameMap.getBlock(mapX, mapY).equals(Block.DOOR) && haskey) {
		    game.completeLevel();
		    return true;
		}
		Block block = gameMap.getBlock(mapX, mapY);
		if(block == null) return true;

		if(!block.equals(Block.FLOOR)) return true;
	    }
	}
	return false;
    }
    


    public void takeDamage(int damage, GameMap gameMap) {
	this.hp-=damage;
	if(this.hp <= 0) {
	    LOGGER.info("Player died");
	    deathSound.play();
	    game.resetMap();
	}
	else hitSound.play();
    }


    public void createRays() {
	final int fov = 60;
	final double angleStep = 0.5;
	double relativeAngle = Math.toDegrees(angle)-fov/2.0;

	for (double index = 0; index <= fov; index+=angleStep) {
	    double angle = Math.toRadians(relativeAngle + index);
	    angle %= (2 * Math.PI);
	    if (angle < 0) {
		angle += 2 * Math.PI;
	    }

	    Ray ray = new Ray(currentPos, angle, gameMap);
	    rays[(int)(index*2)] = ray;
	}
    }

    public Point getCurrentPos() {
	return currentPos;
    }

    public double getAngle() {
	return angle;
    }

    public Ray getRay(int i) {
	return rays[i];
    }

    public int getNumberOfRays() {
	return rays.length;
    }

    public boolean tryPickUp(Interactable object){
	final int pickupDistance = 15;

	if(object.getDistanceToPlayer(this)<pickupDistance && object.isOnMap()) {
	    inventory.add(object);
	    return true;
	}
	return false;
    }

    public Inventory getInventory() {
	return inventory;
    }

    public int getHp() {
	return hp;
    }

    public int getMaxHp() {
	return maxHp;
    }


    public void pickedUpKey() {
	this.haskey = true;
    }
}
