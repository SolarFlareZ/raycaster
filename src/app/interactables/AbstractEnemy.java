package app.interactables;

import app.initialization.Config;
import app.initialization.GameMap;
import app.mechanics.HealthBar;
import app.resourcehandlers.Texture;

import java.awt.*;


/**
* AbstractEnemy is the parent of all enemies, containing methods and fields that child classes can use.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */

public abstract class AbstractEnemy extends AbstractInteractable implements Enemy
{
    protected int damage;
    protected HealthBar healthBar;
    protected int hp;

    /**
     * The constructor for AbstractEnemy
     *
     * @param Point the position of the enemy
     * @param size the size of the enemy
     * @param onMap sets if the enemy is drawn or not
     * @param maxHP the starting HP of the enemy
     * @param damage the damage the enemy deals per hit
     * @param texture the texture which is to represent the enemy
     */
    protected AbstractEnemy(final Point currentPos, final Dimension size, final boolean onMap, final int maxHP, final int damage,
			    final Texture texture) {
	super(currentPos, size, onMap, texture);
	this.hp = maxHP;
	this.damage = damage;
	this.healthBar = new HealthBar(size.width, 10, maxHP);
    }

    @Override public void drawObject(final Graphics g, final double angleDiff, final int distanceToPlayer) {
	super.drawObject(g, angleDiff, distanceToPlayer);
	int frameHeight = Config.FRAME_HEIGHT;

	Rectangle shape = createShape(angleDiff, distanceToPlayer, healthBar.getSize());

	int distanceQuotient = frameHeight * 64 / distanceToPlayer;
	shape.y = frameHeight / 2 - distanceQuotient/2 - shape.height/2;

	healthBar.drawStatic(g, hp, shape);
    }

    public void takeDamage(int damage, GameMap gameMap) {
	this.hp -=damage;
	if(this.hp <= 0) {
	    gameMap.removeInteractable(this);
	    onMap = false;
	}
    }
}
