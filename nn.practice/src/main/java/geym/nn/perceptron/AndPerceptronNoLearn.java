
package geym.nn.perceptron;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class AndPerceptronNoLearn extends NeuralNetwork {
	public AndPerceptronNoLearn(int inputNeuronsCount) {
		this.createNetwork(inputNeuronsCount);
	}

	private void createNetwork(int inputNeuronsCount) {
		// 设置网络类别为 感知机
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);

		// 建立输入神经元 ，表示输入的刺激
		NeuronProperties inputNeuronProperties = new NeuronProperties();
		inputNeuronProperties.setProperty("neuronType", InputNeuron.class);

		// 由输入神经元构成的输入层
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
		this.addLayer(inputLayer);
		// 在输入层增加BiasNeuron，表示神经元偏置
		inputLayer.addNeuron(new BiasNeuron());

		//设置传输函数为step()函数
		NeuronProperties outputNeuronProperties = new NeuronProperties();
		outputNeuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		Layer outputLayer = LayerFactory.createLayer(1, outputNeuronProperties);
		this.addLayer(outputLayer);
		
		//将输入层和输出层进行全连接
		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);
		Neuron n = outputLayer.getNeuronAt(0);
		
		//设置输入神经元和感知机之间的连接权重
		n.getInputConnections()[0].getWeight().setValue(1);
		n.getInputConnections()[1].getWeight().setValue(1);
		n.getInputConnections()[2].getWeight().setValue(-1.5);
	}

	public static void main(String[] args) {
		DataSet trainingSet = new DataSet(2, 1);
		trainingSet.addRow(new DataSetRow(new double[] { 0, 0 }, new double[] { Double.NaN }));
		trainingSet.addRow(new DataSetRow(new double[] { 0, 1 }, new double[] { Double.NaN }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 0 }, new double[] { Double.NaN }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 1 }, new double[] { Double.NaN }));

		AndPerceptronNoLearn perceptron = new AndPerceptronNoLearn(2);

		for (DataSetRow row : trainingSet.getRows()) {
			perceptron.setInput(row.getInput());
			perceptron.calculate();
			double[] networkOutput = perceptron.getOutput();
			System.out.println(Arrays.toString(row.getInput()) + "=" + Arrays.toString(networkOutput));
		}
	}
}
