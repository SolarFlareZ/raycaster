package app.interactables;


import app.initialization.GameMap;
import app.initialization.Player;

/**
 * Weapon extends Interactable by giving object the ability to damage other objects.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public interface Weapon extends Interactable
{
    boolean isAllowedToAttack();
    int getCurrentCooldown();
    int getCooldown();
    void use(Player player, GameMap gameMap);
}
