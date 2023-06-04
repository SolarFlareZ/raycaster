package app.interactables;

import app.mechanics.Animate;
import app.mechanics.Block;
import app.initialization.Config;
import app.initialization.GameMap;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
* The bullet class is used to draw bullets, and to calculate the
* current position of a bullet.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class Bullet extends AbstractInteractable
{
    private Animate shooter;
    private int impactDamage;
    private double angle;

    /**
     * The constructor for Bullet
     *
     * @param currentPosthe starting position of the bullet
     * @param size	the size of the bullet
     * @param onMap	sets if the bullet is to be displayed or not
     * @param shooter	the Animate who shoots
     * @param impactDamge the damage the bullet causes on impact
     * @param angle	the angle which the bullet moves at
     */
    public Bullet(final Point currentPos, final Dimension size, final boolean onMap,
		  Animate shooter, int impactDamage, double angle) {
	super(currentPos, size, onMap, new Texture("images/red_amongus.png"));
	this.shooter = shooter;
	this.impactDamage = impactDamage;
	this.angle = angle;
    }


    public void update(GameMap gameMap, Player player){
	travelToTarget(angle);

	Animate target = hitTarget(gameMap, player);
	if(target != null) removeBullet(target, gameMap);

	if(hitWall(gameMap)) onMap = false;
    }

    private Animate hitTarget(GameMap gameMap, Player player) {
	final int hitRadius = 20;

	if(!shooter.equals(player) && getDistanceToPlayer(player)<hitRadius) return player;

	for (int i = 0; i < gameMap.getNumberOfEnemies(); i++) {
	    Enemy enemy = gameMap.getEnemy(i);
	    if(!enemy.equals(shooter) && getDistanceToPlayer(enemy)<hitRadius) return enemy;
	}
	return null;
    }


    private void travelToTarget(double angle){
	final double travelSpeed = 10.0;
	double travelX = travelSpeed * Math.sin(angle);
	double travelY = travelSpeed * -Math.cos(angle);
	currentPos = new Point(currentPos.x + (int)travelX, currentPos.y + (int)travelY);
    }

    private void removeBullet(Animate target, GameMap gameMap) {
	target.takeDamage(impactDamage, gameMap);
	onMap = false;
    }

    private boolean hitWall(GameMap gameMap) {
    	int posX = currentPos.x / gameMap.getBlockSize().width;
	int posY = currentPos.y / gameMap.getBlockSize().height;
	Block block = gameMap.getBlock(posX, posY);
	if(!block.equals(Block.FLOOR)) return true;
	return false;
    }

    @Override public Rectangle createShape(double angleDiff, int distanceToPlayer, Dimension size) {
	Rectangle shape = super.createShape(angleDiff ,distanceToPlayer, size);
	shape.y = Config.FRAME_HEIGHT / 2;
	return shape;
    }
}
