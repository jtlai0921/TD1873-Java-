
package geym.nn.perceptron;

import java.util.Arrays;
import java.util.Random;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;

/**
 * 识别四象限
 * 
 * @author Administrator
 *
 */
public class PerceptronAxisClassify extends NeuralNetwork implements LearningEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7685380676792200464L;
	static Random r = new Random();

	public static void main(String[] args) {
		new PerceptronAxisClassify().run();
	}

	public static double nextDouble() {
		double re = 0;
		while ((re = r.nextDouble()) != 0) {
			return re;
		}
		return r.nextDouble();
	}

	public void run() {
		DataSet td = new DataSet(2, 2);

		for (int i = 0; i < 10000; i++) {
			// 第一象限
			td.addRow(new DataSetRow(new double[] { 1 * nextDouble(), 1 * nextDouble() }, new double[] { 1, 1 }));
			// 第二象限
			td.addRow(new DataSetRow(new double[] { -1 * nextDouble(), 1 * nextDouble() }, new double[] { 0, 1 }));
			// 第三象限
			td.addRow(new DataSetRow(new double[] { -1 * nextDouble(), -1 * nextDouble() }, new double[] { 0, 0 }));
			// 第四象限
			td.addRow(new DataSetRow(new double[] { 1 * nextDouble(), -1 * nextDouble() }, new double[] { 1, 0 }));
		}
		// 感知机有2个输入，2个输出
		SimplePerceptron2 myPerceptron = new SimplePerceptron2(2);

		PerceptronLearningRule learningRule = (PerceptronLearningRule) myPerceptron.getLearningRule();
		learningRule.setMaxError(0.001);
		learningRule.addListener(this);

		// 进行学习
		System.out.println("Training neural network...");
		myPerceptron.learn(td);

		// 测试感知机是否能给出正确输出
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myPerceptron);
	}

	/**
	 * Prints network output for the each element from the specified training
	 * set.
	 * 
	 * @param neuralNet
	 *            neural network
	 * @param trainingSet
	 *            training set
	 */
	/**
	 * @param neuralNet
	 */
	public static void testNeuralNetwork(NeuralNetwork neuralNet) {
		DataSet td = new DataSet(2, 2);
		for (int i = 0; i < 1000; i++) {
			// 第一象限
			td.addRow(new DataSetRow(new double[] { 1 * nextDouble(), 1 * nextDouble() }, new double[] { 1, 1 }));
			// 第二象限
			td.addRow(new DataSetRow(new double[] { -1 * nextDouble(), 1 * nextDouble() }, new double[] { 0, 1 }));
			// 第三象限
			td.addRow(new DataSetRow(new double[] { -1 * nextDouble(), -1 * nextDouble() }, new double[] { 0, 0 }));
			// 第四象限
			td.addRow(new DataSetRow(new double[] { 1 * nextDouble(), -1 * nextDouble() }, new double[] { 1, 0 }));
		}
		int correctCount = 0;
		int incorrectCount = 0;
		for (DataSetRow testSetRow : td.getRows()) {
			neuralNet.setInput((testSetRow.getInput()));
			neuralNet.calculate();
			double[] networkOutput = neuralNet.getOutput();
			if (Arrays.equals(networkOutput, testSetRow.getDesiredOutput())) {
				correctCount++;
			} else {
				incorrectCount++;
			}
		}
		System.out.println("正确率："+correctCount * 1.0 / (correctCount + incorrectCount));
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		IterativeLearning bp = (IterativeLearning) event.getSource();
		System.out.println("iterate:" + bp.getCurrentIteration());
		System.out.print("TotalNetworkError:");
		System.out.println(((PerceptronLearningRule) bp.getNeuralNetwork().getLearningRule()).getTotalNetworkError());
	}

}
