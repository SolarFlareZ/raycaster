package app.interactables;

import app.resourcehandlers.Texture;

import java.awt.*;
/**
* This class is used to seperate the Key from other interactable objects
 * to avoid type checking.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class Key extends AbstractInteractable
{

    public Key(final Point currentPos, final Dimension size, final boolean onMap, final Texture texture) {
        super(currentPos, size, onMap, texture);
    }
}
