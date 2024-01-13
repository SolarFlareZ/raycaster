package app.interactables;

import app.initialization.Config;
import app.mechanics.Animate;
import app.resourcehandlers.Texture;

import java.awt.*;

/**
* AbstractInteractables sets the rules for all interactable objects in the game.
* Which includes enemies, keys and weapons.
 *
 * @author Oliver Björklund
 * @version 1.0
 */

public abstract class AbstractInteractable implements Interactable
{
    protected Point currentPos;
    protected Dimension size;
    protected boolean onMap;
    protected Texture texture;


    protected AbstractInteractable(final Point currentPos, final Dimension size, final boolean onMap, Texture texture) {
	this.currentPos = currentPos;
	this.size = size;
	this.onMap = onMap;
        this.texture = texture;
    }

    public Rectangle createShape(double angleDiff, int distanceToPlayer, Dimension size) {
        final Dimension frameSize = new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);

        int width = distanceToPlayer!=0?frameSize.width * size.width / distanceToPlayer : frameSize.width;
        int height = distanceToPlayer!=0?frameSize.height * size.height / distanceToPlayer : frameSize.height;
        int xPos = (int) (angleDiff / (Math.PI / 3) * frameSize.width) - width/2;

        int distanceQuotient = frameSize.height*64/distanceToPlayer;
        int yPos = frameSize.height / 2 + distanceQuotient/2 - height/2;

        return new Rectangle(xPos, yPos, width, height);
    }


    public void drawObject(Graphics g, double angleDiff, int distanceToPlayer) {
        if (!onMap) return;
        Graphics2D g2d = (Graphics2D)g;
        Texture texture = getTexture();
        Rectangle rect = createShape(angleDiff, distanceToPlayer, size);
        g2d.drawImage(texture.getImg(), rect.x, rect.y, rect.width, rect.height, null);
    }

    public int getDistanceToPlayer(Animate player) {
        Point playerPos = player.getCurrentPos();
        return (int) Math.hypot(currentPos.x - playerPos.x, currentPos.y - playerPos.y);
    }

    @Override public double getAngleFromPlayer(Point playerPos) {
        double angleFromPlayer;
        int diffX = (playerPos.x - currentPos.x);
        int diffY = (playerPos.y - currentPos.y);
        if (diffX == 0 && diffY > 0) {
            angleFromPlayer = 0;
        }
        else if (diffX == 0 && diffY < 0) {
            angleFromPlayer = Math.PI;
        } else {
            angleFromPlayer = Math.atan2(diffY, diffX)+3*Math.PI/2;
            angleFromPlayer %= (2 * Math.PI);
            if (angleFromPlayer < 0) {
                angleFromPlayer += 2 * Math.PI;
            }
        }
        return angleFromPlayer;
    }

    public void drawInHand(Graphics g) {
        Texture texture = getTexture();
        final int width = 150;
        final int height = 150;
        int posX = Config.FRAME_WIDTH-width;
        int posY = Config.FRAME_HEIGHT-height;
        g.drawImage(texture.getImg(), posX, posY, width, height, null);
    }

    public boolean isOnMap() {
        return onMap;
    }

    public void setOnMap(boolean onMap) {
        this.onMap = onMap;
   }

    public Point getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(final Point currentPos) {
        this.currentPos = currentPos;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isUsable() {
        return false;
    }
}
