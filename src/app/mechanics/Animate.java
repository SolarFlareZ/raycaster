package app.mechanics;

import app.initialization.GameMap;

import java.awt.*;

/**
 * Defines an interface for animate objects.
 * <p>Defines methods for getting hp, maxhp, damage and position</p>
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */

public interface Animate {
    Point getCurrentPos();
    void takeDamage(int damage, GameMap gameMap);
}
