import Game.GlobalSettings;
import Graphics.Screen;
import Helpers.Calc;
import neuralNet_v2.Network;

public class Pong {
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Screen screen;

	Network net;

	boolean done = false;

	double fitness = 0;

	int[] weights;

	int runTime = 1;

	int wins = 0;
	boolean wonGame = false;

	public Pong() {
		this.screen = GlobalSettings.getScreen();
		ball = new Ball(screen.getPixelWidth() / 2, screen.getPixelHeight() / 2, 4);
		paddle1 = new Paddle(20, 2);
		paddle2 = new Paddle(screen.getPixelWidth() - 20, 2);
		ball.addPaddle(paddle1);
		ball.addPaddle(paddle2);
		net = new Network(6, 2, 3, 1);
		// System.out.println(net);
	}

	public void render() {
		ball.render();
		paddle1.render();
		paddle2.render();
	}

	public void update() {
		if (!done) {

			updateNet();

			if (ball.getY() - paddle2.getY() < 0) {
				paddle2.moveUp();
			}
			if (ball.getY() - paddle2.getY() > 0) {
				paddle2.moveDown();
			}

			// updateNet2();

			runTime++;
			ball.update();

		}
	}

	void updateNet() {
		double[] inputs = { Calc.map((float) ball.getY(), 0, screen.getPixelHeight(), -1, 1),
				Calc.map((float) paddle1.getY(), 0, screen.getPixelHeight(), -1, 1),
				Calc.map((float) paddle2.getY(), 0, screen.getPixelHeight(), -1, 1),
				Calc.map((float) ball.getX(), 0, screen.getPixelWidth(), -1, 1), ball.getXVelocity(),
				ball.getYVelocity() };
		double[] output = net.run(inputs);

		//paddle1.setY((int) Calc.map((float) output[0], -1, 1, 0, screen.getPixelHeight()));

		 if (output[0] > 0) {
		 paddle1.moveUp();
		 }
		
		 if (output[0] < 0) {
		 paddle1.moveDown();
		 }

		if (ball.getX() > screen.getPixelWidth() - 10) {
			done = true;
			fitness += 2000;
			wins++;
			wonGame = true;
		} else if (ball.getX() < 10) {
			done = true;
			fitness += 0;
			if (wins >= 2)
				wins = 0;
		} else if (runTime > 2000) {
			done = true;
			fitness += 1000;
			wins -= 1;
		}
	}

	public void runCycle() {
		if (done) {
			runTime = 1;
			fitness = 0;
			wonGame = false;
			ball.reset();
			paddle1.reset();
			paddle2.reset();
			done = false;
		}
	}

	public boolean isDone() {
		return done;
	}

	public double getFitness() {
		return fitness + wins;
	}

	public Network getNetwork() {
		return net;
	}

	public boolean wonGame() {
		return wonGame;
	}
}
