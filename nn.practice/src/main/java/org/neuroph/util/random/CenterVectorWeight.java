package org.neuroph.util.random;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class CenterVectorWeight extends WeightsRandomizer {
	private double[] centerVector=null;
	public CenterVectorWeight(DataSet ds){
		centerVector =new double[ds.getInputSize()];
		for(DataSetRow row:ds.getRows()){
			for(int i=0;i<ds.getInputSize();i++){
				centerVector[i]+=row.getInput()[i];
			}
		}
		for(int i=0;i<ds.getInputSize();i++){
			centerVector[i]/=ds.getRows().size();
		}
	}
	
	@Override
    public void randomize(NeuralNetwork neuralNetwork) {
        for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
        	setWeightVector(neuron);
        }
    }
    
	private void setWeightVector(Neuron neuron) {
		Connection[] inputConnections=neuron.getInputConnections();
		for (int i=0;i<inputConnections.length;i++) {
			double newWeight=centerVector[i]+Math.random()-0.5;
			inputConnections[i].getWeight().setValue(newWeight);
		}
	}
}
