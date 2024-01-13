package app.interactables;


import app.mechanics.Animate;
import app.ai.EnemyAI;
import app.initialization.GameMap;
import app.initialization.Player;

/**
* Defines all the methods that the different enemies
 * have in common.
 * @author Oliver Björklund
 * @version 1.0
 */
public interface Enemy extends Interactable, Animate
{
    void act(Player player, GameMap gameMap, EnemyAI ai);
}
