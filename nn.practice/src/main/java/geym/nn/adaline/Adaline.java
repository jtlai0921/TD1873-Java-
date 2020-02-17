package geym.nn.adaline;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class Adaline extends NeuralNetwork<LMS> {

	/**
	 * The class fingerprint that is set to indicate serialization compatibility
	 * with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Adaline network with specified number of neurons in input
	 * layer
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 */
	public Adaline(int inputNeuronsCount, int outputNeuronsCount) {
		this.createNetwork(inputNeuronsCount, outputNeuronsCount);
	}

	/**
	 * Creates adaline network architecture with specified number of input
	 * neurons
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 */
	private void createNetwork(int inputNeuronsCount, int outputNeuronsCount) {
		this.setNetworkType(NeuralNetworkType.ADALINE);

		NeuronProperties inNeuronProperties = new NeuronProperties();
		inNeuronProperties.setProperty("transferFunction", TransferFunctionType.LINEAR);

		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, inNeuronProperties);
		inputLayer.addNeuron(new BiasNeuron()); 
																	
		this.addLayer(inputLayer);

		NeuronProperties outNeuronProperties = new NeuronProperties();
		 outNeuronProperties.setProperty("transferFunction", TransferFunctionType.LINEAR);

		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, outNeuronProperties);
		this.addLayer(outputLayer);

		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		NeuralNetworkFactory.setDefaultIO(this);

		LMS lms=new LMS();
		lms.setLearningRate(0.05);
		lms.setMaxError(0.5);
		this.setLearningRule(lms);
	}

}
