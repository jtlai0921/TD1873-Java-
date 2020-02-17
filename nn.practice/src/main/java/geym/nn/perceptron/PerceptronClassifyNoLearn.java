
package geym.nn.perceptron;

import java.util.Arrays;
import java.util.Scanner;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * 识别四象限
 * 
 * @author Administrator
 *
 */
public class PerceptronClassifyNoLearn extends NeuralNetwork {

	/**
	 * The class fingerprint that is set to indicate serialization compatibility
	 * with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Perceptron with specified number of neurons in input and
	 * output layer, with Step trqansfer function
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public PerceptronClassifyNoLearn(int inputNeuronsCount) {
		this.createNetwork(inputNeuronsCount);
	}

	/**
	 * Creates perceptron architecture with specified number of neurons in input
	 * and output layer, specified transfer function
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            neuron transfer function type
	 */
	private void createNetwork(int inputNeuronsCount) {
		// 设置网络类别为 感知机
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);

		// 输入神经元建立 ，表示输入的刺激
		NeuronProperties inputNeuronProperties = new NeuronProperties();
		inputNeuronProperties.setProperty("neuronType", InputNeuron.class);

		// 由输入神经元构成的输入层
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
		this.addLayer(inputLayer);

		NeuronProperties outputNeuronProperties = new NeuronProperties();
		outputNeuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		Layer outputLayer = LayerFactory.createLayer(2, outputNeuronProperties);
		this.addLayer(outputLayer);

		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);

		Neuron n = outputLayer.getNeuronAt(0);
		n.getInputConnections()[0].getWeight().setValue(1);
		n.getInputConnections()[1].getWeight().setValue(0);

		n = outputLayer.getNeuronAt(1);
		n.getInputConnections()[0].getWeight().setValue(0);
		n.getInputConnections()[1].getWeight().setValue(1);

	}

	public static String posToString(double[] networkOutput ){
		if((networkOutput[0]+networkOutput[1])==2){
			return "第一象限";
		}else if((networkOutput[0]+networkOutput[1])==0){
			return "第三象限";
		}else if((networkOutput[0]-networkOutput[1])==1){
			return "第四象限";
		}
		return "第二象限";
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String line = null;
		double[] input = new double[2];
		PerceptronClassifyNoLearn perceptron = new PerceptronClassifyNoLearn(2);
		try {
			while ((line = scanner.nextLine()) != null) {
				String[] numbers = line.split("[\\s|,|;]");
				input[0] = Double.parseDouble(numbers[0]);
				input[1] = Double.parseDouble(numbers[1]);
			
				perceptron.setInput(input);
				perceptron.calculate();
				double[] networkOutput = perceptron.getOutput();
				System.out.println(Arrays.toString(input) + "=" + posToString(networkOutput));

			}
		} finally {
			scanner.close();
		}
	}
}
