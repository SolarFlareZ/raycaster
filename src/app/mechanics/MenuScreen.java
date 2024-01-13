package app.mechanics;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Defines a welcomescreen which allows the user to start the game or quit.
 *
 * @author Oliver Björklund
 * @version 1.0
 */
public class MenuScreen extends JPanel {
    private Dimension preferredSize;
    private JButton startButton;
    private JButton exitButton;
    private BufferedImage backgroundImage = null;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    public MenuScreen(Dimension compSize) {
	this.preferredSize = compSize;

	setPreferredSize(preferredSize);
	setLayout(new GridBagLayout());

	GridBagConstraints constraints = new GridBagConstraints();
	constraints.anchor = GridBagConstraints.CENTER;
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	constraints.insets = new Insets(10, 0, 10, 0);

	this.startButton = new JButton("Start Game");

	this.exitButton = new JButton("Exit Game");

	initButtonsStyle(new JButton[] { startButton, exitButton });

	JPanel buttons = new JPanel(new GridBagLayout());
	buttons.add(startButton, constraints);
	buttons.add(exitButton, constraints);
	buttons.setOpaque(false);

	add(buttons, constraints);


	try {
	    URL url = ClassLoader.getSystemResource("images/block.jpg");
	    this.backgroundImage = ImageIO.read(url);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	LOGGER.config("Menuscreen successfully initialized");
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
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

    private void initButtonsStyle(JButton[] buttons) {
	for (JButton button : buttons) {
	    button.setPreferredSize(new Dimension(preferredSize.width/4, preferredSize.height/12));
	    button.setBackground(Color.GRAY);
	    button.setForeground(Color.WHITE);
	}
    }
}
