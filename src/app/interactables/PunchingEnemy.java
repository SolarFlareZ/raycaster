package app.interactables;

import app.ai.EnemyAI;
import app.initialization.GameMap;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * PunchingEnemy is a melee-enemy that packs a deadly punch.
 * The class uses arguments that can determine its position, size, damage and health to create
 * a variety of enemies.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class PunchingEnemy extends AbstractEnemy
{

    private int reach;

    /**
     * The constructor of PunchingEnemy
     *
     * @param Point the position of the enemy
     * @param size the size of the enemy
     * @param onMap sets if the enemy is drawn or not
     * @param maxHP the starting HP of the enemy
     * @param damage the damage the enemy deals per hit
     * @param texture the texture which is to represent the enemy
     */
    public PunchingEnemy(final Point currentPos, final Dimension size, final boolean onMap, final int maxHP, final int damage,
			 final Texture texture) {
	super(currentPos, size, onMap, maxHP, damage, texture);
	this.reach = 60;
    }

    private boolean isInRange(Player player) {
	int distX = Math.abs(player.getCurrentPos().x-currentPos.x);
	int distY = Math.abs(player.getCurrentPos().y-currentPos.y);

	if(distX<reach && distY<reach) return true;
	return false;
    }

    private void punch(Player player, GameMap gameMap) {
	player.takeDamage(damage, gameMap);
    }

    public void act(Player player, GameMap map, EnemyAI ai) {
	final int scanDistance = 700;

	if(isInRange(player) && ai.isPlayerVisible() && ai.getAttackDelay() < 0) {
	    final int attackDelay = 60;
	    punch(player, map);
	    ai.setAttackDelay(attackDelay);
	} else if(getDistanceToPlayer(player)<scanDistance){
	    ai.findPath();
	}
    }
}
