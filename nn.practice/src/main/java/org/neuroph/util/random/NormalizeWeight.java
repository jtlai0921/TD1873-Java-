package org.neuroph.util.random;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.util.random.WeightsRandomizer;

public class NormalizeWeight extends WeightsRandomizer {
    public void randomize(NeuralNetwork neuralNetwork) {
        for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
        	normalize(neuron);
        }
    }

    public void normalize(Neuron neuron){
    	double sum=0;
    	for (Connection connection : neuron.getInputConnections()) {
    		sum+=connection.getWeight().getValue()*connection.getWeight().getValue();
    	}
    	double mod=Math.sqrt(sum);
    	for (Connection connection : neuron.getInputConnections()) {
    		connection.getWeight().setValue(connection.getWeight().getValue()/mod);
    	}
    }
    
}
