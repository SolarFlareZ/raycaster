package app.resourcehandlers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Creates a playable sound object.
 */
public class Jukebox {
    private Clip clip = null;
    private static final Logger LOGGER = Logger.getLogger("GameLogger");

    public Jukebox(String path) {
	while(true) {
	    try{
		AudioInputStream stream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(path));
		this.clip = AudioSystem.getClip();
		clip.open(stream);
		break;
	    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
		LOGGER.warning("Unable to load sound effect");
		e.printStackTrace();
		int answer = JOptionPane.showConfirmDialog(null, "Unable to sound. Would you like to try again?",
							   "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(answer == JOptionPane.NO_OPTION) break;
	    }
	}

    }

    public void play() {
	if (clip != null) {
	    clip.setFramePosition(0);
	    clip.start();
	}
    }
}
