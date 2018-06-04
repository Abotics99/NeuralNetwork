package neuralNet_v2;

import Helpers.Calc;

public class Layer {

	Node[] nodes;
	private double bias;

	private boolean isInput = false;
	private int nodeCount;

	public Layer(int nodeCount, double bias, boolean isThisAnInput) {
		nodes = new Node[nodeCount];
		this.nodeCount = nodeCount;
		this.bias = bias;
		isInput = isThisAnInput;
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(this);
		}
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public Node getNode(int index) {
		return nodes[index];
	}

	public boolean isInput() {
		return isInput;
	}

	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}

	public void clearNodes() {
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].clear();
		}
	}

	public void setNodeValues(double[] input) {
		for (int i = 0; i < input.length; i++) {
			nodes[i].setValue(input[i]);
		}
	}

	public double[] getNodeValues() {
		double[] temp = new double[nodes.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = nodes[i].getValue();
		}
		return temp;
	}

	public void mutate(double mutationRate) {
		bias += Calc.RandomRange(-mutationRate, mutationRate);
	}
}
