package app.interactables;

import app.ai.EnemyAI;
import app.initialization.GameMap;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;
/**
* ShootingEnemy is a basic punchy melee enemy. They can be created with various amont of health and damage.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class ShootingEnemy extends AbstractEnemy
{
    /**
     *	The constuctor of ShootingEnemy
     *
     *   @param Point the position of the enemy
     *   @param size the size of the enemy
     *   @param onMap sets if the enemy is drawn or not
     *   @param hp the starting HP of the enemy
     *   @param damage the damage the enemy deals per hit
     *   @param texture the texture which is to represent the enemy
     */
    public ShootingEnemy(final Point currentPos, final Dimension size, final boolean onMap, final int hp, final int damage, final Texture texture) {
	super(currentPos, size, onMap, hp, damage, texture);
    }

    public void shoot(Player player, GameMap gameMap) {
	Point target = player.getCurrentPos();
	double angle = convertRadians(getAngleFromPlayer(target) - Math.PI);
	Bullet bullet = new Bullet(currentPos, new Dimension(5, 5), true,
				   this, damage, angle);
	bullet.update(gameMap, player);
	gameMap.addObject(bullet);
	gameMap.addBullet(bullet);
    }


    private double convertRadians(double angle) {
	angle %= (2 * Math.PI);
	if (angle < 0) {
	    angle += 2 * Math.PI;
	}
	return angle;
    }


    public void act(Player player, GameMap map, EnemyAI ai) {
	final int targetDistance = 200;
	final int scanDistance = 600;

	boolean isPlayerVisible = ai.isPlayerVisible();
	int distance = getDistanceToPlayer(player);

	if (isPlayerVisible && ai.getAttackDelay()<0) {
	    final int attackDelay = 60;
	    shoot(player, map);
	    ai.setAttackDelay(attackDelay);
	}

	if((!isPlayerVisible || distance>targetDistance)
	   && distance<scanDistance) ai.findPath();
    }
}
