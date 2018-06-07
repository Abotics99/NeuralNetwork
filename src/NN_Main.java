import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.Game;
import Game.GlobalSettings;
import Graphics.Screen;
import Helpers.SaveManager;

/*TODO
 * -button.render outta bounds exception protection 
 */

public class NN_Main implements Runnable {

	Screen screen = new Screen();

	// --gameSettings--

	// --gameData--
	long lastUpdateTime = 0;

	// --icon data--
	BufferedImage fontData = null;

	// --other objects--\
	Game game;

	// --jus' Declarin--
	public final boolean FASTMODE = false; // disables rendering and cycles as fast as it can

	static SaveManager save = new SaveManager("saveData");
	static SaveManager log = new SaveManager("growthLogs");

	public static void main(String[] args) {
		log.saveData(new String[] { "-- this is the beginning of the the growth logs --" });
		new NN_Main().start();
	}

	public void run() {
		screen.init();
		GlobalSettings.setScreen(screen);
		init();
		while (GlobalSettings.running) {
			update();
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("bye...");
		System.exit(0);
	}

	public synchronized void start() {
		GlobalSettings.running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		GlobalSettings.running = false;
	}

	public void init() {
		try {
			fontData = ImageIO.read(new File(System.getProperty("user.dir"), "/res/Font.png"));
		} catch (IOException e) {
			System.out.println("Could not find fontData image.");
		}
		GlobalSettings.setFontData(fontData);
		// --initialize objects here--
		game = new Game();
		GlobalSettings.setGame(game);
	}

	public void update() {
		long now = System.nanoTime();
		double nsPerTick = 1;

		nsPerTick = 1000000000 / 60;
		

		if (now - lastUpdateTime > nsPerTick) {
			lastUpdateTime = now;
			tick();
			render();
		}
	}

	// -- fun stuff below --

	void render() {
		if (!FASTMODE) {
			screen.background(new Color(35,58,71, 255));
			game.render();
			screen.render();
		}
	}

	public void tick() {
		game.update();
	}
}
