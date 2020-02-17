/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.input.Difference;
import org.neuroph.core.transfer.Linear;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.util.*;
import org.neuroph.util.random.CenterVectorWeight;

/**
 * Kohonen neural network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Kohonen extends NeuralNetwork {
	/**
	 * The class fingerprint that is set to indicate serialization compatibility
	 * with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Kohonen network with specified number of neurons in input and
	 * map layer
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public Kohonen(int inputNeuronsCount, int outputNeuronsCount) {
		this.createNetwork(inputNeuronsCount, outputNeuronsCount);
	}

	/**
	 * Creates Kohonen network architecture with specified number of neurons in
	 * input and map layer
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	protected void createNetwork(int inputNeuronsCount, int outputNeuronsCount) {
		NeuronProperties inputNeuronProperties = new NeuronProperties();
		NeuronProperties outputNeuronProperties = createOutputNeuronProperties();
		this.setNetworkType(NeuralNetworkType.KOHONEN);
		
		Layer inLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
		this.addLayer(inLayer);
		Layer mapLayer = LayerFactory.createLayer(outputNeuronsCount, outputNeuronProperties);
		this.addLayer(mapLayer);
		ConnectionFactory.fullConnect(inLayer, mapLayer);
		NeuralNetworkFactory.setDefaultIO(this);

		KohonenLearning kl = new KohonenLearning();
		kl.setIterations(100, 50);
		this.setLearningRule(kl);
	}

	protected NeuronProperties createOutputNeuronProperties() {
		return new NeuronProperties(Neuron.class, Difference.class, Linear.class);
	}

	@Override
	public void learn(DataSet trainingSet) {
		preLearn(trainingSet);
		super.learn(trainingSet);
	}

	public void preLearn(DataSet trainingSet) {
		randomizeWeights(new CenterVectorWeight(trainingSet));
	}
}
