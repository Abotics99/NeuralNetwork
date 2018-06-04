import java.awt.Color;
import java.util.ArrayList;

import Game.GlobalSettings;
import Graphics.Screen;
import Graphics.Sprite;
import Helpers.Calc;

public class Ball {

	private int startPosX;
	private int startPosY;

	private double posX;
	private double posY;
	private double velocityX;
	private double velocityY;
	private double speed = 2;
	private double startSpeed = 2;
	private double ballAcceleration = 0.0;
	private boolean flashing = false;
	long frameCount = 0;

	private ArrayList<Paddle> paddles = new ArrayList<Paddle>();
	Screen screen;

	int[][] spr = { { 9 } };

	Sprite ball;
	
	private int hitCount = 0;

	public Ball(int startX, int startY, int startSpeed) {
		startPosX = startX;
		startPosY = startY;
		this.screen = GlobalSettings.getScreen();
		this.startSpeed = startSpeed;
		reset();
		ball = new Sprite(spr, new Color(255, 255, 255), 1);
	}

	public void addPaddle(Paddle paddle) {
		paddles.add(paddle);
	}

	public void update() {
		calcCollision();
		checkPadCollisions();
		posX += velocityX * speed;
		posY += velocityY * speed;
		
		if(Math.abs(velocityX) < 0.1) {
			setDir(velocityX*2,velocityX);
		}
		speed+=ballAcceleration;
	}

	public void render() {
		frameCount++;
		if (flashing) {
			if (frameCount % 15 == 0)
				ball.isHidden(!ball.isHidden());
		} else {
			ball.isHidden(false);
		}
		ball.draw((int) (posX - 5), (int) (posY - 5));
	}

	public void reset() {
		posX = startPosX;
		posY = startPosY;
		speed = startSpeed;
		frameCount = 0;
		setDir(Calc.RandomRange(45, 135));

		//setDir(45);
		hitCount = 0;
	}

	private void calcCollision() {
		if (posX < 0 && velocityX < 0) {
			velocityX *= -1;
		}
		if (posX > screen.getPixelWidth() && velocityX > 0) {
			velocityX *= -1;
		}
		if (posY < 0 && velocityY < 0) {
			velocityY *= -1;
		}
		if (posY > screen.getPixelHeight() && velocityY > 0) {
			velocityY *= -1;
		}
	}
	
	public int getHitCount() {
		return hitCount;
	}

	private void checkPadCollisions() {
		for (Paddle pad : paddles) {
			if (pad.getX() < screen.getPixelWidth() / 2) {
				if (Math.abs(posX - pad.getX()) < 5 && Math.abs(posY - pad.getY()) < pad.getHeight() * 5
						&& velocityX < 0) {
					setDir(posX - pad.getX(), (posY - pad.getY()) / 2);
					hitCount++;
				}
			} else {
				if (Math.abs(posX - pad.getX()) < 5 && Math.abs(posY - pad.getY()) < pad.getHeight() * 5
						&& velocityX > 0) {
					setDir(posX - pad.getX(), (posY - pad.getY()) / 2);
				}
			}
		}
	}

	public void setDir(double deg) {
		velocityX = Math.sin(Math.toRadians(deg));
		velocityY = Math.cos(Math.toRadians(deg));
	}

	public void setDir(double x, double y) {
		double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		velocityX = x / distance;
		velocityY = y / distance;
	}

	public double getX() {
		return posX;
	}

	public double getY() {
		return posY;
	}

	public boolean isFlashing() {
		return flashing;
	}

	public void setFlashing(boolean flashing) {
		this.flashing = flashing;
	}
	
	public double getXVelocity() {
		return velocityX * speed;
	}

	public double getYVelocity() {
		return velocityY * speed;
	}
}
