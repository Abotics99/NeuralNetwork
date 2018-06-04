package Game;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	// Each sound effect has its own clip, loaded with its own sound file.
	private Clip clip;

	// Constructor to construct each element of the enum with its own sound file.
	Sound(String name) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/" + name + ".wav").toURI().toURL());
			// Get a clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Play or Re-play the sound effect from the beginning, by rewinding.
	public void play() {
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
		clip.start(); // Start playing
	}
	
	public void stop() {
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
	}
	
	public void loop() {
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public boolean isPlaying() {
		return clip.isRunning();
	}
}