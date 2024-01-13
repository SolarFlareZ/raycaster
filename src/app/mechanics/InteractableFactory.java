package app.mechanics;

import app.interactables.Enemy;
import app.interactables.EnemyType;
import app.interactables.Gun;
import app.interactables.GunType;
import app.interactables.PunchingEnemy;
import app.interactables.ShootingEnemy;
import app.interactables.Sword;
import app.interactables.SwordType;
import app.resourcehandlers.Jukebox;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
 * A factory for creating weapons and enemies.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class InteractableFactory {
    public Gun createGun(GunType type, Point pos, Dimension size, boolean onMap) {
	return switch (type) {
	    case GUN -> new Gun(pos, size, onMap, 10, 1000, new Texture("images/gun.png"),
				 new Jukebox("audio/gun.wav"));
	    case RIFLE -> new Gun(pos, size, onMap, 30, 5000, new Texture("images/rifle.png"),
				 new Jukebox("audio/rifle.wav"));
	    case CROSSBOW -> new Gun(pos, size, onMap, 50, 7000, new Texture("images/crossbow.png"),
				 new Jukebox("audio/crossbow.wav"));
	    case BOMB -> new Gun(pos, size, onMap, 70, 10000, new Texture("images/bomb.png"),
				 new Jukebox("audio/bomb.wav"));
	    default -> throw new IllegalArgumentException("There is no gun of that type");
	};
    }

    public Sword createSword(SwordType type, Point pos, Dimension size, boolean onMap) {
	return switch (type) {
	    case MINECRAFT_SWORD -> new Sword(pos, size, onMap, 10, 1000, new Texture("images/minecraft_sword.png"),
					      new Jukebox("audio/minecraft_sword.wav"),100);
	    case SCYTHE -> new Sword(pos, size, onMap, 30, 3000, new Texture("images/scythe.png"),
				     new Jukebox("audio/scythe.wav"), 200);
	    case DAGGER -> new Sword(pos, size, onMap, 5, 300, new Texture("images/knife.png"),
				     new Jukebox("audio/knife.wav"), 100);
	    case FROSTMOURNE -> new Sword(pos, size, onMap, 100, 15000, new Texture("images/frostmourne.png"),
					  new Jukebox("audio/frostmourne.wav"), 100);
	    default -> throw new IllegalArgumentException("There is no sword of that type");
	};
    }

    public Enemy createEnemy(EnemyType type, Point pos, Dimension size, boolean onMap) {
	return switch (type) {
	    case RED_AMONG_US -> new PunchingEnemy(pos, size, onMap, 100, 10, new Texture("images/red_amongus.png"));
	    case GHOST -> new PunchingEnemy(pos, size, onMap, 40, 20, new Texture("images/pacman.png"));
	    case ALIEN -> new ShootingEnemy(pos, size, onMap, 400, 10, new Texture("images/spaceinvader.png"));
	    case JAVA -> new ShootingEnemy(pos, size, onMap, 500, 20, new Texture("images/java.png"));
	    default -> throw new IllegalArgumentException("There is no enemy of that type");
	};
    }
}
