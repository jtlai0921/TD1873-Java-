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

import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.norm.VectorNormalizer;
import org.neuroph.core.input.DotProduct;
import org.neuroph.core.transfer.ArcCos;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.random.CenterVectorWeight;
import org.neuroph.util.random.NormalizeWeight;

public class KohonenCos extends Kohonen {
	private static final long serialVersionUID = 1L;

	public KohonenCos(int inputNeuronsCount, int outputNeuronsCount) {
		super(inputNeuronsCount, outputNeuronsCount);
	}

	protected NeuronProperties createOutputNeuronProperties() {
		return new NeuronProperties(Neuron.class, DotProduct.class, ArcCos.class);
	}

	@Override
	public void preLearn(DataSet trainingSet) {
		new VectorNormalizer().normalize(trainingSet);
		randomizeWeights(new CenterVectorWeight(trainingSet));
		randomizeWeights(new NormalizeWeight());
	}
}
