package app.interactables;

import app.ai.EnemyAI;
import app.initialization.GameMap;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * Defines all common methods for the enemies which doesn't shoot.
 * @author Oliver Björklund
 * @version 1.0
 */
public class PunchingEnemy extends AbstractEnemy
{

    private int reach;


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
