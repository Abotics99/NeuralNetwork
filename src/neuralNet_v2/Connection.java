package neuralNet_v2;

import Helpers.Calc;

public class Connection {
	private Node node1;
	private Node node2;
	private double weight;
	
	public Connection(Node node1, Node node2, double weight) {
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}
	
	public void run() {
		node2.addValue(node1.getValue() * weight);
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void mutate(double mutationAmount) {
		weight += Calc.RandomRange(-mutationAmount, mutationAmount);
	}

}
