package app.interactables;

import app.initialization.GameMap;
import app.resourcehandlers.Jukebox;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * Sword is used as the foundation to all melee weapons in the game.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class Sword extends AbstractWeapon
{
    private int range;


    public Sword(final Point currentPos, final Dimension size, final boolean onMap, int damage, int cooldown, Texture texture,
		 Jukebox sound, int range) {
	super(currentPos, size, onMap, damage, cooldown, texture, sound);
	this.range = range;
    }

    public void use(Player player, GameMap gameMap) {
	if(!isAllowedToAttack()) return;
	setCooldown();
	playSound();
	for (int i = 0; i < gameMap.getNumberOfEnemies(); i++) {
	    Enemy enemy = gameMap.getEnemy(i);
	    if(isInRange(player, enemy)) enemy.takeDamage(hitDamage, gameMap);
	}
    }

    private boolean isInRange(Player player, Enemy enemy) {
	Point playerPos = player.getCurrentPos();
	Point enemyPos = enemy.getCurrentPos();
	int distX = Math.abs(enemyPos.x-playerPos.x);
	int distY = Math.abs(enemyPos.y-playerPos.y);

	if(Math.hypot(distX, distY) <= range) return true;
	return false;
    }
}
