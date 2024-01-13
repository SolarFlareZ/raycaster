package app.interactables;

import app.mechanics.Animate;
import app.resourcehandlers.Texture;

import java.awt.*;
/**
* Interface for all interactable objects.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public interface Interactable {
    double getAngleFromPlayer(Point playerPos);

    int getDistanceToPlayer(Animate player);

    void drawObject(Graphics g, double angleDiff, int distanceToPlayer);

    boolean isOnMap();
    void setOnMap(boolean onMap);
    Texture getTexture();
    Point getCurrentPos();
    void setCurrentPos(Point currentPos);
    void drawInHand(Graphics g);
    boolean isUsable();
}
