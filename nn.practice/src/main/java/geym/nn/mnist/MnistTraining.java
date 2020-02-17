package geym.nn.mnist;

import java.io.IOException;
import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import geym.nn.mlperceptron.MlPerceptron;
import geym.nn.util.Utils;

public class MnistTraining implements LearningEventListener {
	public static final String MnistDir = "src\\main\\resources\\handswriter\\mnist";

	public static void main(String[] args) throws IOException {
		new MnistTraining().run();
	}

	public void run() throws IOException {
		DataSet trainingSet = MnistReader.trainingData(MnistDir + "\\train-images.idx3-ubyte",
				MnistDir + "\\train-labels.idx1-ubyte", 60000);

		MlPerceptron myMlPerceptron = new MlPerceptron(TransferFunctionType.SIGMOID, 784, 100,10);
		// 设置可接收的误差
		BackPropagation  learningRule = myMlPerceptron.getLearningRule();
		learningRule.setLearningRate(0.05);
		learningRule.setMaxError(0.001d);
		learningRule.setMaxIterations(3);
		learningRule.addListener(this);

		System.out.println("Training neural network...");
		myMlPerceptron.learn(trainingSet);

		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myMlPerceptron);
	}

	public static void testNeuralNetwork(NeuralNetwork neuralNet) throws IOException {
		DataSet testDataSet = MnistReader.trainingData(MnistDir + "\\t10k-images.idx3-ubyte",
				MnistDir + "\\t10k-labels.idx1-ubyte", 10000);
		int rightCount = 0;
		int i=0;
		for (; i < testDataSet.size(); i++) {
			neuralNet.setInput(testDataSet.getRowAt(i).getInput());
			neuralNet.calculate();
			double[] networkOutput = neuralNet.getOutput();
			// 将活跃度最高的输出视为1，其余视为0
			networkOutput = Utils.competition(networkOutput);
			if (Arrays.equals(networkOutput, testDataSet.getRowAt(i).getDesiredOutput())) {
				rightCount++;
			}
			if(i%1000==0)
			    System.out.println("正确率:" + rightCount * 1.0 / (i+1));
		}
		System.out.println("正确率:" + rightCount * 1.0 / (i+1));
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();
		System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
	}
}
