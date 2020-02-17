package geym.nn.pca;

import java.io.IOException;
import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import geym.nn.mlperceptron.MlPerceptron;
import geym.nn.mnist.MnistReader;
import geym.nn.mnist.MnistTraining;
import geym.nn.util.Utils;

public class MnistPca implements LearningEventListener {
	public static int dim = 20;

	public void makeAveZero(DataSet trainingSet) {
		int inputCount = trainingSet.getRowAt(0).getInput().length;
		int rowCount = trainingSet.getRows().size();
		double[] ave = new double[inputCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < inputCount; j++) {
				ave[j] += trainingSet.getRowAt(i).getInput()[j];
			}
		}

		for (int i = 0; i < ave.length; i++) {
			ave[i] /= rowCount;
		}

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < inputCount; j++) {
				trainingSet.getRowAt(i).getInput()[j] -= ave[j];
			}
		}
	}

	public void run() throws IOException {
		DataSet trainingSet = MnistReader.trainingData(MnistTraining.MnistDir + "\\train-images.idx3-ubyte",
				MnistTraining.MnistDir + "\\train-labels.idx1-ubyte", 60000);

		makeAveZero(trainingSet);

		SangerNetwork sanger = new SangerNetwork(trainingSet.getRowAt(0).getInput().length, dim);

		SangerLearning learningRule = (SangerLearning) sanger.getLearningRule();
		learningRule.setLearningRate(0.001);
		learningRule.setMaxIterations(6);
		learningRule.addListener(new LearningEventListener() {
			@Override
			public void handleLearningEvent(LearningEvent event) {
				IterativeLearning bp = (IterativeLearning) event.getSource();
				System.out.println("iterate:" + bp.getCurrentIteration());
				System.out.println(Arrays.toString(bp.getNeuralNetwork().getWeights()));
			}
		});

		// 进行学习
		System.out.println("Training pca neural network...");
		sanger.learn(trainingSet);
		System.out.println("end of Training pca neural network...");

		DataSet pcaDateSet = pca(sanger, trainingSet);

		MlPerceptron myMlPerceptron = new MlPerceptron(TransferFunctionType.SIGMOID, dim, 40, 10);
		// 设置可接收的误差
		BackPropagation learningRule1 = myMlPerceptron.getLearningRule();
		learningRule1.setLearningRate(0.01);
		learningRule1.setMaxError(0.05d);
		learningRule1.setMaxIterations(100);
		learningRule1.addListener(this);

		System.out.println("Training neural network...");
		myMlPerceptron.learn(pcaDateSet);

		testNeuralNetwork(myMlPerceptron, sanger);
	}

	public void testNeuralNetwork(NeuralNetwork neuralNet, SangerNetwork sanger) throws IOException {
		DataSet testDataSet = MnistReader.trainingData(MnistTraining.MnistDir + "\\t10k-images.idx3-ubyte",
				MnistTraining.MnistDir + "\\t10k-labels.idx1-ubyte", 10000);

		makeAveZero(testDataSet);

		DataSet pcaDateSet = pca(sanger, testDataSet);
		int rightCount = 0;
		int i = 0;
		for (; i < pcaDateSet.size(); i++) {
			neuralNet.setInput(pcaDateSet.getRowAt(i).getInput());
			neuralNet.calculate();
			double[] networkOutput = neuralNet.getOutput();
			// 将活跃度最高的输出视为1，其余视为0
			networkOutput = Utils.competition(networkOutput);
			if (Arrays.equals(networkOutput, testDataSet.getRowAt(i).getDesiredOutput())) {
				rightCount++;
			}
			if (i % 1000 == 0)
				System.out.println("正确率:" + rightCount * 1.0 / (i + 1));
		}
		System.out.println("正确率:" + rightCount * 1.0 / (i + 1));
	}

	public static DataSet pca(NeuralNetwork sanger, DataSet testSet) {
		DataSet trainingSet = new DataSet(dim, 10);
		for (DataSetRow testSetRow : testSet.getRows()) {
			sanger.setInput(testSetRow.getInput());
			sanger.calculate();
			trainingSet.addRow(new DataSetRow(sanger.getOutput().clone(), testSetRow.getDesiredOutput().clone()));
		}
		return trainingSet;
	}

	public static void main(String[] args) throws IOException {
		new MnistPca().run();
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();
		System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
	}

}
