package neuralNet_v2;

public class Network {
	Layer[] layers;
	Connection[] connections;

	public Network(int inputs, int hiddenLayers, int nodesPerHiddenLayer, int outputs) {
		layers = new Layer[hiddenLayers + 2];
		// initialize layers
		layers[0] = new Layer(inputs, 0, true);
		for (int i = 1; i < layers.length - 1; i++) {
			layers[i] = new Layer(nodesPerHiddenLayer, 0, false);
		}
		layers[layers.length - 1] = new Layer(outputs, 0, false);
		// initialize connections in this robo-brain
		connections = new Connection[(inputs * nodesPerHiddenLayer)
				+ ((int) Math.pow(nodesPerHiddenLayer, 2) * (hiddenLayers - 1)) + (nodesPerHiddenLayer * outputs)];
		attachConnections();
	}

	private void attachConnections() {// initialize all the connections between layers and nodes
		int connectionIndex = 0;
		for (int i = 0; i < layers.length - 1; i++) {
			for (int j = 0; j < layers[i].getNodeCount(); j++) {
				for (int k = 0; k < layers[i + 1].getNodeCount(); k++) {
					connections[connectionIndex++] = new Connection(layers[i].getNode(j), layers[i + 1].getNode(k), 0);
				}
			}
		}
	}

	public double[] run(double[] inputs) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].clearNodes();
		}
		layers[0].setNodeValues(inputs);
		for (int i = 0; i < connections.length; i++) {
			connections[i].run();
		}
		return layers[layers.length - 1].getNodeValues();
	}

	public void mutateAll(double mutationAmount) {
		for (int i = 0; i < connections.length; i++) {
			connections[i].mutate(mutationAmount);
		}
		for (int i = 0; i < layers.length; i++) {
			layers[i].mutate(mutationAmount);
		}
	}

	public void mutateBias(double mutationAmount) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].mutate(mutationAmount);
		}
	}

	public void mutateWeights(double mutationAmount) {
		for (int i = 0; i < connections.length; i++) {
			connections[i].mutate(mutationAmount);
		}
	}

	public double[] getWeights() {
		double[] temp = new double[connections.length];
		for (int i = 0; i < connections.length; i++) {
			temp[i] = connections[i].getWeight();
		}
		return temp;
	}

	public void setWeights(double[] weights) {
		for (int i = 0; i < connections.length; i++) {
			connections[i].setWeight(weights[i]);
		}
	}

	public double[] getBiases() {
		double[] temp = new double[layers.length - 1];
		for (int i = 1; i < layers.length; i++) {
			temp[i-1] = layers[i].getBias();
		}
		return temp;
	}

	public void setBiases(double[] weights) {
		for (int i = 1; i < layers.length; i++) {
			layers[i].setBias(weights[i-1]);
		}
	}
}
