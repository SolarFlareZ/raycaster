package app.interactables;

import app.resourcehandlers.Jukebox;
import app.resourcehandlers.Texture;

import javax.swing.*;
import java.awt.*;

/**
 *  AbstractWeapon contains methods and fields which are used for all weapons.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public abstract class AbstractWeapon extends AbstractInteractable implements Weapon
{
    protected boolean allowedToAttack;
    protected int hitDamage;
    protected int cooldown;
    protected Jukebox sound;
    protected int currentCooldown;

    /**
     * The constructor of AbstractWeapon
     *
     * @param currentPos the Point where the weapon will spawn
     * @param size 	the dimension of the Weapon
     * @param onMap 	sets if the weapon is to be displayed or not
     * @param hitDamage sets the damage of the weapon
     * @param cooldown 	the cooldown between attacks with the weapon
     * @param texture	the texture that will be displayed for the weapon
     * @param sound 	the sound that will be played when attacking with the weapon
     */
    protected AbstractWeapon(final Point currentPos, final Dimension size, final boolean onMap, int hitDamage, int cooldown, Texture texture,
			     final Jukebox sound) {
	super(currentPos, size, onMap, texture);
	this.hitDamage = hitDamage;
	this.cooldown = cooldown;
	this.currentCooldown = 0;
	this.sound = sound;
	this.allowedToAttack = true;
    }

    public void setCooldown() {
	allowedToAttack = false;
	this.currentCooldown = cooldown;
	final int cooldownTick = 100;

	Timer timer = new Timer(cooldownTick, e -> {
	    this.currentCooldown-=cooldownTick;
	    if(currentCooldown<=0) {
		this.allowedToAttack = true;
		Timer parentTimer = (Timer)e.getSource();
		parentTimer.stop();
	    }
	});
	timer.setCoalesce(true);
	timer.start();
    }

    public boolean isAllowedToAttack() {
	return allowedToAttack;
    }


    public void playSound() {
	sound.play();
    }
    public int getCurrentCooldown() {
	return currentCooldown;
    }

    @Override public int getCooldown() {
	return cooldown;
    }

    @Override public boolean isUsable() {
	return true;
    }
}
