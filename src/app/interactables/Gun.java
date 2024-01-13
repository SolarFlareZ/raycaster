package app.interactables;

import app.initialization.GameMap;
import app.resourcehandlers.Jukebox;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * This class represents all the weapons which is used to shoot.
 * This means that it handles the act event where bullets are created.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class Gun extends AbstractWeapon
{

    public Gun(final Point currentPos, final Dimension size, final boolean onMap, int damage, int cooldown, Texture texture,
	       Jukebox sound) {
	super(currentPos, size, onMap, damage, cooldown, texture, sound);
    }

    @Override public void use(Player shooter, GameMap gameMap) {
	if(!isAllowedToAttack()) return;


	Point pos = shooter.getCurrentPos();
	double angle = shooter.getAngle();

	Bullet bullet = new Bullet(pos, size, true, shooter, hitDamage, angle);

	bullet.update(gameMap, shooter);
	gameMap.addObject(bullet);
	gameMap.addBullet(bullet);

	setCooldown();
	playSound();
    }
}
