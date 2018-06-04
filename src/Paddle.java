import java.awt.Color;

import Game.GlobalSettings;
import Graphics.Screen;
import Graphics.Sprite;

public class Paddle {
	
	Screen screen;

	private int height = 3;
	private int moveSpeed = 2;

	private int posY;
	private int posX;

	int[][] spr = { { 179, 179, 179 } };
	Sprite paddle;

	public Paddle(int posX,int speed) {
		this.screen = GlobalSettings.getScreen();
		this.posX = posX;
		this.posY = screen.getPixelHeight() / 2;
		moveSpeed = speed;
		paddle = new Sprite(spr, new Color(255, 255, 255), 1);
	}

	public void update() {

	}

	public void moveUp() {
		posY -= moveSpeed;
		posY = clamp(height * 5, screen.getPixelHeight() - height * 5, posY);
	}

	public void moveDown() {
		posY += moveSpeed;
		posY = clamp(height * 5, screen.getPixelHeight() - height * 5, posY);
	}
	
	public void setY(int posY) {//from -1 to 1
		this.posY = posY;
		this.posY = clamp(height * 5, screen.getPixelHeight() - height * 5, posY);
	}

	public void render() {
		paddle.draw(posX - 5, posY - (height * 5));
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	private int clamp(int min, int max, int val) {
		return Math.max(Math.min(val, max), min);
	}
	
	public void reset() {
		posY = screen.getPixelHeight() / 2;
	}
}
