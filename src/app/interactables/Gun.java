package app.interactables;

import app.initialization.GameMap;
import app.resourcehandlers.Jukebox;
import app.initialization.Player;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * Gun is class representing all shooting-weapons. It takes 7 arguments. a Point which determines where on the map the gun is spawned.
 * Dimension will set the size of the drawn gun. The boolean onMap is used to determine if its spawned on the map or not.
 * An integer sets its damage, another int sets its cooldown, Texture sets its texture and Jukebox determines its sound-effect.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class Gun extends AbstractWeapon
{
    /**
     * The constructor of Gun
     *
     * @param currentPos the spawning location of the gun
     * @param size	the size of the displayed gun
     * @param onMap	sets if the gun is to be displayed on the map or not
     * @param damage	the damage of the gun
     * @param cooldown	the cooldown of the gun
     * @param texture	the texture which represents the gun
     * @param sound	the sound that is played when the gun is used
     */
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
