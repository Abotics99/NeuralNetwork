package Game;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	// Each sound effect has its own clip, loaded with its own sound file.
	private Clip clip;
	
	boolean sound3D = false;
	
	int soundX = 0;
	int soundY = 0;

	// Constructor to construct each element of the enum with its own sound file.
	Sound(String name) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File("res/" + name + ".wav").toURI().toURL());
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
	
	public void update() {
		
	}

	// Play or Re-play the sound effect from the beginning, by rewinding.
	public void play() {
		sound3D = false;
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
		clip.start(); // Start playing
	}
	
	public void playAt(int x, int y) {
		sound3D = true;
		soundX = x;
		soundY = y;
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
		sound3D = false;
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void loopAt(int x, int y) {
		sound3D = true;
		soundX = x;
		soundY = y;
		if (clip.isRunning())
			clip.stop(); // Stop the player if it is still running
		clip.setFramePosition(0); // rewind to the beginning
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public boolean isPlaying() {
		return clip.isRunning();
	}

	public void setGain(float gain) {
		((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(gain);
	}
	
	public void setPan(float pan) {
		((FloatControl) clip.getControl(FloatControl.Type.PAN)).setValue(pan);
	}
}