import java.awt.Color;

import Game.GlobalSettings;
import Graphics.Screen;

public class Population {
	int population;
	Pong[] pong;

	boolean generationDone;
	
	Screen screen;

	public Population(int population) {
		this.screen = GlobalSettings.getScreen();
		this.population = population;
		pong = new Pong[population];
		for (int i = 0; i < pong.length; i++) {
			pong[i] = new Pong();
		}
	}

	public void render() {
		for (int i = 0; i < pong.length; i++) {
			pong[i].render();
		}
		screen.background(new Color(0,0,0,200));
		pong[pong.length-1].render();
	}

	public void update() {
		if (!generationDone) {
			for (int i = 0; i < pong.length; i++) {
				pong[i].update();
			}
			if(allDone()) {
				insertionSort(pong);
				mutate(100);
				//mutateHalf();
				generationDone = true;
			}
		}
	}
	
	public void mutateHalf() {
		for(int i =pong.length/2; i < pong.length;i++) {
			pong[i - pong.length/2].getNetwork().setWeights(pong[i].getNetwork().getWeights());
			pong[i - pong.length/2].getNetwork().setBiases(pong[i].getNetwork().getBiases());
			pong[i - pong.length/2].getNetwork().mutateAll(0.01 * (1 - winPercent()));
		}
	}
	
	public void mutate(int copyLength) {
		for(int i =0; i < pong.length - copyLength;i++) {
			pong[i].net.setWeights(pong[(pong.length - 1) - (i % copyLength)].net.getWeights());
			pong[i].net.setBiases(pong[(pong.length - 1) - (i % copyLength)].net.getBiases());
			pong[i].net.mutateAll(0.01 * (1 - winPercent()));
		}
	}
	
	public void insertionSort(Pong[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int j = i;
			while (j > 0 && arr[j].getFitness() < arr[j - 1].getFitness()) {
				swap(arr, j, j - 1);
				j--;
			}
		}
	}
	
	public void swap(Pong[] arr, int index1, int index2) {
		Pong tempIndex = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = tempIndex;
	}

	public void startGeneration() {
		if (generationDone) {
			generationDone = false;
			startAll();
		}
	}
	
	public double bestFitness() {
		return pong[pong.length-1].getFitness();
	}
	
	public boolean isDone() {
		return generationDone;
	}

	private void startAll() {
		for (int i = 0; i < pong.length; i++) {
			pong[i].runCycle();
		}
	}

	public boolean allDone() {
		for (int i = 0; i < pong.length; i++) {
			if (!pong[i].isDone()) {
				return false;
			}
		}
		return true;
	}
	
	public String getAllFitnesses() {
		String temp = "";
		temp += pong[0].getFitness();
		for(int i =1;i<pong.length;i++) {
			temp += ", " + pong[i].getFitness();
		}
		return temp;
	}
	
	public double winPercent() {
		int temp = 0;
		for(Pong tempPong:pong) {
			if(tempPong.wonGame()) {
				temp++;
			}
		}
		return (double)temp / population;
	}
	
	public double[] getBestWeights() {
		return pong[pong.length-1].getNetwork().getWeights();
	}
	
	public double[] getBestBias() {
		return pong[pong.length-1].getNetwork().getBiases();
	}
}
