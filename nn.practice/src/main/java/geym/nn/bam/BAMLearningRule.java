package geym.nn.bam;

import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;

public class BAMLearningRule extends LearningRule {

	private static final long serialVersionUID = -8865664992399456616L;

	@Override
	public void learn(DataSet trainingSet) {
		int M = trainingSet.size();
		for (int i = 0; i < M; i++) {
			DataSetRow row = trainingSet.getRowAt(i);
			learnRow(row);
		}
	}

	public void learnRow(DataSetRow row) {
		for (int i = 0; i < row.getInput().length; i++) {
			for (int j = 0; j < row.getDesiredOutput().length; j++) {
				Neuron ini = neuralNetwork.getLayerAt(0).getNeuronAt(i);
				Neuron outj = neuralNetwork.getLayerAt(1).getNeuronAt(j);
				outj.getConnectionFrom(ini).getWeight().value += row.getInput()[i] * row.getDesiredOutput()[j];
				ini.getConnectionFrom(outj).getWeight().value = outj.getConnectionFrom(ini).getWeight().value;
			}
		}
	}

}
