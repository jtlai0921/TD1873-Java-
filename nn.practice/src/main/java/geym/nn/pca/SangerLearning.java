package geym.nn.pca;

import java.util.Iterator;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.stop.StopCondition;
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;

public class SangerLearning extends UnsupervisedHebbianLearning {
	private double delta = 0;
	private Double[] lastItrWeights;

	public class MinWeightChangeStopCondition implements StopCondition {
		private IterativeLearning learningRule;

		public MinWeightChangeStopCondition(IterativeLearning learningRule) {
			this.learningRule = learningRule;
		}

		@Override
		public boolean isReached() {
		    System.out.println("delta="+delta);
			return delta < 0.000001;
		}

	}

	@Override
	protected void beforeEpoch() {
		lastItrWeights = this.getNeuralNetwork().getWeights();
	}

	@Override
	protected void afterEpoch() {
		Double[] currentWeights = this.getNeuralNetwork().getWeights();
		delta = 0;
		for (int i = 0; i < currentWeights.length; i++) {
			delta += Math.pow((currentWeights[i] - lastItrWeights[i]), 2);
		}
		delta = Math.sqrt(delta);
	}

	@Override
	public void doLearningEpoch(DataSet trainingSet) {
		Iterator<DataSetRow> iterator = trainingSet.iterator();
		while (iterator.hasNext() && !isStopped()) {
			DataSetRow trainingSetRow = iterator.next();
			learnPattern(trainingSetRow);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		stopConditions.add(new MinWeightChangeStopCondition(this));
	}

	protected int index(Neuron neuron) {
		Neuron[] outputNeurons = neuralNetwork.getOutputNeurons();
		for (int i = 0; i < outputNeurons.length; i++) {
			if (neuron.equals(outputNeurons[i])) {
				return i;
			}
		}
		return -1;
	}

	protected Neuron index(int i) {
		return neuralNetwork.getOutputNeurons()[i];
	}

	@Override
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();
		int i = index(neuron);
		for (int k = 0; k < neuron.getInputConnections().length; k++) {
			Connection connection = neuron.getInputConnections()[k];
			double input = connection.getInput();
			double yw = 0;
			//前n个神经元Y*W之和
			for (int n = 0; n < i; n++) {
				yw += index(n).getOutput() * index(n).getInputConnections()[k].getWeight().getValue();
			}
			input -= yw;
			double weight = connection.getWeight().getValue();
			double deltaWeight = (input - output * weight) * output * this.learningRate;
			connection.getWeight().inc(deltaWeight);
		}
	}
}
