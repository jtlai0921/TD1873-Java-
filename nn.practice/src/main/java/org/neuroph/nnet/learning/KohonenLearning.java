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

package org.neuroph.nnet.learning;

import java.util.Iterator;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.KohonenCos;
import org.neuroph.util.random.NormalizeWeight;

/**
 * Learning algorithm for Kohonen network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class KohonenLearning extends LearningRule {

	/**
	 * The class fingerprint that is set to indicate serialization compatibility
	 * with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	double learningRate = 0.9d;
	int[] iterations = { 100, 50 };
	double decStep[] = new double[2];
	int mapSize = 0;
	int[] nR = { 2, 1 }; // 邻域半径
	int currentIteration;

	public KohonenLearning() {
		super();
	}

	@Override
	public void learn(DataSet trainingSet) {
		for (int phase = 0; phase < 2; phase++) {
			for (int k = 0; k < iterations[phase]; k++) {
				Iterator<DataSetRow> iterator = trainingSet.iterator();
				while (iterator.hasNext() && !isStopped()) {
					DataSetRow trainingSetRow = iterator.next();
					learnPattern(trainingSetRow, nR[phase]);
					if (this.neuralNetwork instanceof KohonenCos) {
						neuralNetwork.randomizeWeights(new NormalizeWeight());
					}
				} // while
				currentIteration = k;
				this.fireLearningEvent(new LearningEvent(this));
				if (isStopped())
					return;
			} // for k
			learningRate = learningRate * 0.5;
		} // for phase
	}

	private void learnPattern(DataSetRow dataSetRow, int neighborhood) {
		neuralNetwork.setInput(dataSetRow.getInput());
		neuralNetwork.calculate();
		Neuron winner = getClosest();
		Layer mapLayer = neuralNetwork.getLayerAt(1);
		int winnerIdx = mapLayer.indexOf(winner);
		adjustCellWeights(winner, 0);

		int cellNum = mapLayer.getNeuronsCount();
		for (int p = 0; p < cellNum; p++) {
			if (p == winnerIdx)
				continue;
			int distance = distance(winnerIdx, p);
			if (isNeighbor(distance, neighborhood)) {
				Neuron cell = mapLayer.getNeuronAt(p);
				adjustCellWeights(cell, distance);
			} // if
		} // for
	}

	// get unit with closetst weight vector
	private Neuron getClosest() {
		Neuron winner = new Neuron();
		double minOutput = 100;
		for (Neuron n : this.neuralNetwork.getLayerAt(1).getNeurons()) {
			double out = n.getOutput();
			if (out < minOutput) {
				minOutput = out;
				winner = n;
			} // if
		} // while
		return winner;
	}

	private void adjustCellWeights(Neuron cell, int r) {
		for (Connection conn : cell.getInputConnections()) {
			double dWeight = (learningRate / (r + 1)) * (conn.getInput() - conn.getWeight().getValue());
			conn.getWeight().inc(dWeight);
		} // while
	}

	public int distance(int i, int j) {
		int ix = i / mapSize;
		int iy = i % mapSize;
		int jx = j / mapSize;
		int jy = j % mapSize;
		return Math.max(Math.abs(ix - jx), Math.abs(iy - jy));
	}

	/**
	 * 
	 * @param i
	 *            winner Index 中央神经元
	 * @param j
	 *            要判定的神经元
	 * @param n
	 *            领域半径
	 * @return
	 */
	public boolean isNeighbor(int i, int j, int n) {
		return distance(i, j) <= n;
	}

	public boolean isNeighbor(int distance, int n) {
		return distance <= n;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setIterations(int Iphase, int IIphase) {
		this.iterations[0] = Iphase;
		this.iterations[1] = IIphase;
	}

	public Integer getIteration() {
		return new Integer(currentIteration);
	}

	public int getMapSize() {
		return mapSize;
	}

	@Override
	public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
		super.setNeuralNetwork(neuralNetwork);
		int neuronsNum = neuralNetwork.getLayerAt(1).getNeuronsCount();
		mapSize = (int) Math.sqrt(neuronsNum);
	}

}
