package org.neuroph.core.input;

import java.io.Serializable;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class DotProduct extends InputFunction implements Serializable {
	private static final long serialVersionUID = 4561088278548437895L;

	@Override
	public double getOutput(Connection[] inputConnections) {
		double sum = 0d;
		for (Connection connection : inputConnections) {
			Neuron neuron = connection.getFromNeuron();
			Weight weight = connection.getWeight();
			sum +=  neuron.getOutput() * weight.getValue();
		}
		return sum;
	}
}
