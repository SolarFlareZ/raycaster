package app.mechanics;

import app.initialization.GameMap;

import java.awt.*;

/**
 * Defines an interface for animate objects.
 *
 * @author Oliver Björklund
 * @version 1.0
 */

public interface Animate {
    Point getCurrentPos();
    void takeDamage(int damage, GameMap gameMap);
}
