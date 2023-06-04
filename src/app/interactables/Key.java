package app.interactables;

import app.resourcehandlers.Texture;

import java.awt.*;
/**
* Creates a key-object used to unlock doors. Key(Point,Dimension,Boolean,Texture), Point sets where it spawns,
 * dimension sets its size, boolean sets if its on the map, Texture sets which texture is used to draw the Key.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class Key extends AbstractInteractable
{
    /**
     * The Constructor of Key
     *
     * @param currentPos    the spawning location of the Key
     * @param size          the size of the key
     * @param onMap         sets if the key is to be displayed on the map or not
     * @param texture       the texture of the key
     */
    public Key(final Point currentPos, final Dimension size, final boolean onMap, final Texture texture) {
        super(currentPos, size, onMap, texture);
    }
}
