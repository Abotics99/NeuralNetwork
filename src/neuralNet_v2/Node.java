package neuralNet_v2;

public class Node {
	
	private double value = 0;
	private Layer layer;

	public Node(Layer layer) {
		this.layer = layer;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void addValue(double val) {
		value += val;
	}

	public void clear() {
		value = 0;
	}

	public double getValue() {
		if (layer.isInput()) {
			return value;
		} else {
			return Math.tanh(value + layer.getBias());
		}
	}

}
