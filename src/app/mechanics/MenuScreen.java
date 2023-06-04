package app.mechanics;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * MenuScreen shows which selections are possible for the user.
 *
 * @author Oliver Björklund, Jonathan Eriksson
 * @version 1.0
 */
public class MenuScreen extends JPanel {
    private Dimension preferredSize;
    private JButton startButton;
    private JButton exitButton;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    /**
     * The constructor for MenuScreen
     *
     * @param compSize the size of the menu screen
     */
    public MenuScreen(Dimension compSize) {
	this.preferredSize = compSize;
	setPreferredSize(preferredSize);
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	this.startButton = new JButton("Start Game");
	this.exitButton = new JButton("Exit Game");

	add(startButton);
	add(exitButton);
	LOGGER.config("Menuscreen successfully initialized");
    }

    @Override public Dimension getPreferredSize() {
	return preferredSize;
    }

    public JButton getStartButton() {
	return startButton;
    }

    public JButton getExitButton() {
	return exitButton;
    }
}
