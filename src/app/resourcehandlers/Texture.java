package app.resourcehandlers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Texture is used to load textures into the game. Texture(String), the argument String refers to
 * the path of the texture which is to be used, for example "directory/fileName.type"
 */
public class Texture {
    private int[] pixels;
    private Dimension size;
    private BufferedImage img = null;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    public Texture(final String path) {
	while(true) {
	    try{
		URL url = ClassLoader.getSystemResource(path);
		BufferedImage img = ImageIO.read(url);
		if(img!=null) this.img = img;
		else System.exit(1);
		break;
	    } catch (IOException e) {
		LOGGER.severe("Unable to load textures");
		e.printStackTrace();
		int answer = JOptionPane.showConfirmDialog(null, "Unable to load textures. Would you like to try again?",
							   "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(answer == JOptionPane.NO_OPTION) System.exit(1);
	    }
	}

	this.size = new Dimension(img.getWidth(), img.getHeight());
	this.pixels = img.getRGB(0, 0, size.width, size.height, null, 0, size.width);
    }


    public int[] getPixels() {
	return pixels;
    }

    public Dimension getSize() {
	return size;
    }

    public BufferedImage getImg() {
	return img;
    }
}
