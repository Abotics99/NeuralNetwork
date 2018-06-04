package Game;

import java.awt.Color;

import Graphics.Sprite;

public class Projectile implements Transform{
	private double posX = 0;
	private double posY = 0;
	private double lastPosX = posX;
	private double lastPosY = posY;
	private double speed = 5;
	private double returnSpeed = 1;
	private double velX = 0;
	private double velY = 0;
	private Sprite projectile;
	private Sprite world;

	private boolean returning = false;
	private boolean hidden = true;
	
	Sound returnSound;
	Sound shootSound;

	public Projectile(Sprite world) {
		projectile = new Sprite(new int[][] { { 7 } }, Color.WHITE, 1);
		this.world = world;
		returnSound = new Sound("projectileReturn");
		shootSound = new Sound("shoot");
	}

	public void render() {
		projectile.draw((int) posX, (int) posY);
		projectile.isHidden(hidden);
	}

	public void update(double x, double y) {
		if(returning) {
			double tempX = x - posX;
			double tempY = y - posY + 5;
			double dist = Math.sqrt(Math.pow(tempX, 2) + Math.pow(tempY, 2));
			velX = tempX / dist;
			velY = tempY / dist;
			returnSpeed += 0.25;
			if(dist < returnSpeed) {
				if(returning && !hidden) {
					returnSound.play();
				}
				returning = false;
				hidden = true;
				returnSpeed = 1;
				
			}
			posX += velX * returnSpeed;
			posY += velY * returnSpeed;
		}else {
			posX += velX * speed;
			posY += velY * speed;
		}
		
		checkWorldCollisions(world);
		if(!hidden) {
		checkEnemyCollisions();
		}
		
		lastPosX = posX;
		lastPosY = posY;
		
	}
	
	void checkWorldCollisions(Sprite world) {
		if(!returning && world.getTile((int)posX + 5, (int)posY + 5) == 200) {
			returning = true;
		}
	}
	
	void checkEnemyCollisions() {
		double steps = (Helpers.Calc.dist(lastPosX, lastPosY, posX, posY) / 10);
		for(int i=0;i<steps;i++) {
			for(Enemy enemy: GlobalSettings.getEnemies()) {
				if(enemy.colliding(Helpers.Calc.lerp(lastPosX, posX, i/steps) + 5, Helpers.Calc.lerp(lastPosY, posY, i/steps) + 5)) {
					enemy.damage(50, this);
				}
			}
		}
	}
	
	

	public void shoot(double x, double y, double dirX, double dirY) {
		if(!hidden && !returning) {
			returning = true;
		}
		if (hidden) {
			posX = x;
			posY = y;
			double dist = Helpers.Calc.dist(0, 0, dirX, dirY);
			velX = dirX/dist;
			velY = dirY/dist;
			returning = false;
			hidden = false;
			shootSound.play();
			GlobalSettings.getScreen().shake(1, 1);
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
