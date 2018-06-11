package Game;

import java.awt.Color;

import Graphics.AnimatedSprite;
import Graphics.Sprite;

public class Projectile implements Transform {
	private double posX = 0;
	private double posY = 0;
	private double speed = 5;
	private double returnSpeed = 1;
	private double velX = 0;
	private double velY = 0;
	private AnimatedSprite projectile;
	private Sprite world;
	private int returnTime = 40;
	private int timer = 0;

	private boolean returning = false;
	private boolean hidden = true;
	private boolean falling = false;
	private boolean grounded = false;

	Sound returnSound;
	Sound shootSound;
	Sound hitWallSound;
	Sound landingSound;

	int[][] spin_0 = new int[][] { { 325 } };
	int[][] spin_1 = new int[][] { { 326 } };
	int[][] spin_2 = new int[][] { { 327 } };
	int[][] spin_3 = new int[][] { { 328 } };

	public Projectile(Sprite world) {
		projectile = new AnimatedSprite(new int[][][] { spin_0, spin_1, spin_2, spin_3 }, Color.WHITE, 1, true, 3,
				false, false);
		this.world = world;
		returnSound = new Sound("projectileReturn");
		shootSound = new Sound("shoot");
		hitWallSound = new Sound("impact1");
		landingSound = new Sound("landing_2");
	}

	public void render() {
		projectile.draw((int) posX, (int) posY);
		projectile.isHidden(hidden);
	}

	public void update(double x, double y) {
		if (timer > 0 && !falling) {
			timer--;
			if (timer == 0) {
				returning = true;
				falling = false;
			}
		}
		if (returning) {
			timer = 0;
			double tempX = x - posX;
			double tempY = y - posY;
			double dist = Math.sqrt(Math.pow(tempX, 2) + Math.pow(tempY, 2));
			velX = tempX / dist;
			velY = tempY / dist;
			returnSpeed += 0.25;
			if (dist < returnSpeed) {
				if (returning && !hidden) {
					returnSound.play();
				}
				returning = false;
				hidden = true;
				falling = false;
				returnSpeed = 1;

			}
			posX += velX * returnSpeed;
			posY += velY * returnSpeed;
		} else if (falling) {
			velY += 0.05;
			velX *= 0.95;
			updateCollisions(world, 200, -1, -1, 11, 11);
			posX += velX * speed;
			posY += velY * speed;
		} else {
			posX += velX * speed;
			posY += velY * speed;
		}
		if (!returning && velY > 10) {
			returning = true;
		}

		checkWorldCollisions(world);
		if (!hidden) {
			checkEnemyCollisions();
			projectile.setPause(Math.abs(velY) < 0.05 && Math.abs(velX) < 0.05);
		}
	}

	void checkWorldCollisions(Sprite world) {
		if (!returning && !falling && world.getTile((int) posX + 5, (int) posY + 5) == 200) {
			falling = true;
			velY = -0.5;
			velX = -velX / 2;
			if (!hidden) {
				hitWallSound.play();
			}
		}
	}

	public void updateCollisions(Sprite spr, int colIndex, int xMin, int yMin, int xMax, int yMax) {
		if (velY > 0 && checkCol((int) posX + xMin, (int) posY + yMax + 1, (int) posX + xMax, (int) posY + yMax + 1,
				spr, colIndex)) {
			if (Math.abs(velY) > 0.05) {
				velY = -velY / 4;
			} else {
				velY = 0;
			}
			if (!grounded) {
				grounded = true;
				landingSound.play();
			}
		} else {
			grounded = false;
		}
		if (velX < 0 && checkCol((int) posX + xMin - 1, (int) posY + yMin, (int) posX + xMin - 1, (int) posY + yMax,
				spr, colIndex)) {
			velX = 0;
		}
		if (velX > 0 && checkCol((int) posX + xMax + 1, (int) posY + yMin, (int) posX + xMax + 1, (int) posY + yMax,
				spr, colIndex)) {
			velX = 0;
		}
	}

	boolean checkCol(int x, int y, Sprite spr, int colIndex) {
		return spr.getTile(x, y) == colIndex;
	}

	boolean checkCol(int x1, int y1, int x2, int y2, Sprite spr, int colIndex) {
		double steps = Helpers.Calc.dist(x1, y1, x2, y2) / 10;
		for (int i = 0; i <= steps; i++) {
			if (spr.getTile((int) Helpers.Calc.lerp(x1, x2, i / steps),
					(int) Helpers.Calc.lerp(y1, y2, i / steps)) == colIndex) {
				return true;
			}
		}
		return false;
	}

	void checkEnemyCollisions() {
		double steps = (Helpers.Calc.dist(0, 0, velX, velY) / 10);
		for (Enemy enemy : GlobalSettings.getEnemies()) {
			for (int i = 0; i < steps; i++) {
				if (enemy.colliding(Helpers.Calc.lerp(posX - velX, posX, i / steps) + 5,
						Helpers.Calc.lerp(posY - velY, posY, i / steps) + 5)) {
					enemy.damage(50, this);
				}
			}
		}
	}

	public void shoot(double x, double y, double dirX, double dirY) {
		if (!hidden && !returning) {
			returning = true;
			falling = false;
		}
		if (hidden) {
			posX = x;
			posY = y;
			double dist = Helpers.Calc.dist(0, 0, dirX, dirY);
			velX = dirX / dist;
			velY = dirY / dist;
			returning = false;
			hidden = false;
			falling = false;
			shootSound.play();
			GlobalSettings.getScreen().shake(1, 1);
			timer = returnTime;
		}
	}

	public boolean isHidden() {
		return hidden;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getWidth() {
		return projectile.getWidth();
	}

	public double getHeight() {
		return projectile.getHeight();
	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}
}
