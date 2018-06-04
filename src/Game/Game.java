package Game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Graphics.Button;
import Graphics.Fader;
import Graphics.Screen;
import Graphics.Sprite;

public class Game {

	BufferedImage fontData;
	Screen screen;

	Button play;
	Button exit;
	Button restart;
	Sprite title;
	Sprite deathScreen;
	Sprite world;

	double titleBob = 0;

	int frameCounter = 0;

	boolean paused = false;

	boolean movingPlayer = false;
	Player player;

	int startX = 201;
	int startY = 380;
	int playerNextPosX = startX;
	int playerNextPosY = startY;

	int freezeFrame = 0;

	Fader fader;

	boolean pauseMenu = false;
	
	Sound mainTheme;
	ArrayList<CheckPoint> checkPoints = new ArrayList<CheckPoint>();

	public Game() {
		this.fontData = GlobalSettings.getFontData();
		this.screen = GlobalSettings.getScreen();
		play = new Button(5, screen.getPixelHeight() - 85, 40, 40, new Color(30, 121, 141), 16);
		exit = new Button(5, screen.getPixelHeight() - 40, 40, 35, new Color(30, 121, 141), 232);
		restart = new Button(50, screen.getPixelHeight() - 40, 40, 35, new Color(30, 121, 141), 279);
		title = new Sprite(new int[][] { { 233, 234, 235, 236, 237, 238, 239 }, { 249, 250, 251, 252, 253, 254, 255 } },
				Color.WHITE, 3);
		deathScreen = new Sprite(new int[][] { { 320, 321, 322, 323, 324}, { 336, 337, 338, 339, 340} }, Color.WHITE, 3);
		world = new Sprite(Helpers.ArrayLoader.loadArray("World_1"), Color.WHITE, 1);
		player = new Player(startX, startY, world);
		setCamera(player.getPosX(), player.getPosY());
		initEnemies();
		checkPoints.add(new CheckPoint(766, 428, player));
		checkPoints.add(new CheckPoint(978, 757, player));
		checkPoints.add(new CheckPoint(70, 668, player));
		fader = new Fader(10, new Color(35, 58, 71, 255));
		pauseMenu = true;
		fader.fadeInstant();
		mainTheme = new Sound("MainSong");
		
	}
	
	void initEnemies() {
		addBat(920, 315);
		addBat(1110, 395);
		addBat(1168, 395);
		addBat(1033, 465);
		addBat(1155, 495);
		addBat(1235, 495);
		addBat(1330, 595);
		addBat(1221, 635);
		addBat(887, 685);
		addBat(903, 765);
		addBat(817, 785);
		addBat(798, 695);
		addBat(727, 715);
		addBat(705, 715);
		addBat(789, 469);
		addBat(383, 735);
		addBat(266, 765);
		addBat(205, 775);
		addBat(184, 835);
		addBat(139, 929);
		addBat(106, 1035);
		addBat(17, 1065);
		
		addHound(1177, 695);
		addHound(1812, 515);
		addHound(856, 195);
		addHound(1321, 225);
		addHound(289, 835);
		addHound(78, 975);
	}
	
	void addBat(int x, int y) {
		GlobalSettings.addEnemy(new Bat(x, y, player, 100, 1, 70));
	}
	
	void addHound(int x, int y) {
		GlobalSettings.addEnemy(new BoneHound(x, y, player, 50, 200, 2));
	}

	public void render() {
		world.draw(0, 0);
		player.render();
		renderEnemies();
		renderCheckpoints();
		fader.render();
		if (pauseMenu && fader.isHidden()) {
			play.render();
			exit.render();
			restart.render();
			title.draw((int) (25 + GlobalSettings.getScreen().getCamX()),
					(int) (titleBob + 20 + GlobalSettings.getScreen().getCamY()));
		}
		if(player.isDead() && fader.isHidden()) {
			exit.render();
			restart.render();
			deathScreen.draw((int) (25 + GlobalSettings.getScreen().getCamX()),
					(int) (titleBob + 20 + GlobalSettings.getScreen().getCamY()));
		}
	}

	public void update() {
		if (freezeFrame-- <= 0) {
			if (!paused && fader.isTotallyVisible()) {
				lerpCamera(player.getPosX(), player.getPosY(), 0.8);
				titleBob = Math.sin(frameCounter * 0.05) * 5;
				if (screen.escapePressed() && !player.isDead()) {
					pauseMenu = true;
					fader.fade();
				}
				if (screen.rightPressed()) {
					player.moveRight();
				}
				if (screen.leftPressed()) {
					player.moveLeft();
				}
				player.jump(screen.jumpPressed());
				if (screen.firePressed()) {
					int x = 0;
					int y = 0;
					if (screen.leftPressed())
						x--;
					if (screen.rightPressed())
						x++;
					if (screen.downPressed())
						y = 1;
					if (screen.upPressed())
						y = -1;
					player.shoot(true, x, y);
				} else {
					player.shoot(false, 0, 0);
				}
				player.update();
				updateEnemies();
				updateCheckpoints();
				
				if(player.isDead() && Math.abs(player.playerVelX)<0.1) {
					fader.fade();
				}
			}
			frameCounter++;
		}

		if (fader.isHidden() && movingPlayer) {
			player.setPos(playerNextPosX, playerNextPosY);
			setCamera(player.getPosX(), player.getPosY());
			fader.show();
			movingPlayer = false;
		}

		if (pauseMenu && fader.isHidden()) {
			if (play.isPressed() || screen.escapePressed()) {
				fader.show();
				pauseMenu = false;
			}
			if (exit.isPressed()) {
				GlobalSettings.running = false;
			}
			if (restart.isPressed()) {
				pauseMenu = false;
				respawnPlayer(0);
			}
		}
		
		if(player.isDead() && fader.isHidden()) {
			if (restart.isPressed()) {
				pauseMenu = false;
				player.setRespawn(startX, startY);
				player.setDead(false);
				respawnPlayer(0);
			}
			if (exit.isPressed()) {
				GlobalSettings.running = false;
			}
		}
		
		if(fader.isTotallyVisible()) {
			if(!mainTheme.isPlaying()) {
				mainTheme.loop();
			}
		}else {
			if(mainTheme.isPlaying()) {
				mainTheme.stop();
			}
		}
	}

	public void setCamera(double x, double y) {
		screen.setCameraPos(x - screen.getPixelWidth() / 2, y - screen.getPixelHeight() / 2);
	}

	public void lerpCamera(double x, double y, double speed) {
		screen.setCameraPos(Helpers.Calc.lerp(x - screen.getPixelWidth() / 2, screen.getCamX(), speed),
				Helpers.Calc.lerp(y - screen.getPixelHeight() / 2, screen.getCamY(), speed));
	}

	public void movePlayer(int x, int y) {
		playerNextPosX = x;
		playerNextPosY = y;
		movingPlayer = true;
		fader.fade();
	}

	private void updateEnemies() {
		for (Enemy enemy : GlobalSettings.getEnemies()) {
			enemy.update();
		}
	}

	private void renderEnemies() {
		for (Enemy enemy : GlobalSettings.getEnemies()) {
			enemy.render();
		}
	}
	
	private void updateCheckpoints() {
		for (CheckPoint cp : checkPoints) {
			cp.update();
		}
	}

	private void renderCheckpoints() {
		for (CheckPoint cp : checkPoints) {
			cp.render();
		}
	}

	public void addFreezeFrames(int freezeFrame) {
		this.freezeFrame = freezeFrame;
	}

	public Sprite getCurrentWorld() {
		return world;
	}

	public void respawnPlayer(int damage) {
		if (damage > 0) {
			player.damage(damage);
		}
		movePlayer(player.getStartX(), player.getStartY());
	}
}
