package geym.nn.bam;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.NeuralNetworkCalculatedEvent;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.comp.neuron.InputOutputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class BAM extends NeuralNetwork<LearningRule> {
	private static final long serialVersionUID = 1L;
	
	public void switchInputOutput() {
		Neuron[] tmp = this.getInputNeurons();
		this.setInputNeurons(this.getOutputNeurons());
		this.setOutputNeurons(tmp);
		Layer[] layers = getLayers();
		Layer t = layers[0];
		layers[0] = layers[1];
		layers[1] = t;
	}

	public void switchInputOutputData(DataSetRow row) {
		double[] t = row.getInput();
		row.setInput(row.getDesiredOutput());
		row.setDesiredOutput(t);
	}

	public boolean propagateLayer(DataSetRow row) {
		this.setInput(row.getInput());
		for (Layer layer : this.getLayers()) {
			layer.calculate();
		}
		boolean stable = true;
		double[] output = this.getOutput();
		for (int i = 0; i < output.length; i++) {
			if (output[i] != row.getDesiredOutput()[i]) {
				row.getDesiredOutput()[i] = output[i];
				stable = false;
			}
		}
		return stable;
	}

	public void calculate(DataSetRow row) {
		boolean stable1 = true, stable2 = true;
		do {
			stable1 = propagateLayer(row);
			switchInputOutput();
			switchInputOutputData(row);
			stable2 = propagateLayer(row);
			switchInputOutput();
			switchInputOutputData(row);
		} while (!stable1|| !stable2);
		fireNetworkEvent(new NeuralNetworkCalculatedEvent(this));
	}

	/**
	 * Creates an instance of BAM network with specified number of neurons in
	 * input and output layers.
	 *
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public BAM(int inputNeuronsCount, int outputNeuronsCount) {
		// init neuron settings for BAM network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", InputOutputNeuron.class);
		neuronProperties.setProperty("bias", new Double(0));
		neuronProperties.setProperty("transferFunction", TransferFunctionType.SGN);
		this.createNetwork(inputNeuronsCount, outputNeuronsCount, neuronProperties);
	}

	/**
	 * Creates BAM network architecture
	 *
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int inputNeuronsCount, int outputNeuronsCount, NeuronProperties neuronProperties) {
		this.setNetworkType(NeuralNetworkType.BAM);
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, neuronProperties);
		this.addLayer(inputLayer);
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, neuronProperties);
		this.addLayer(outputLayer);
		ConnectionFactory.fullConnect(inputLayer, outputLayer, 0);
		ConnectionFactory.fullConnect(outputLayer, inputLayer, 0);
		NeuralNetworkFactory.setDefaultIO(this);
		this.setLearningRule(new BAMLearningRule());
	}
}
