package app.mechanics;

import app.initialization.GameMap;
import app.initialization.Player;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
* Defines a minimap which displays how the 3d game is rendered using rays.
 * @author Oliver Björklund
 * @version 1.0
 */

public class MiniMap extends JComponent {
    private GameMap gameMap;
    private Player player;
    private Dimension preferredSize;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");


    public MiniMap(final Player player, final GameMap gameMap, final Dimension size) {
	this.gameMap = gameMap;
	this.player = player;
	this.preferredSize = size;
	LOGGER.config("Minimap successfully initialized");
    }

    @Override public Dimension getPreferredSize() {
	return preferredSize;
    }



    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	player.createRays();
	gameMap.draw(g);
	drawRays(g);
	player.draw(g);
    }

    private void drawRays(Graphics g) {
	for (int i = 0; i < player.getNumberOfRays(); i++) {
	    Ray ray = player.getRay(i);
	    ray.draw(g);
	}
    }

    public void setGameMap(final GameMap gameMap) {
	this.gameMap = gameMap;
    }
}
