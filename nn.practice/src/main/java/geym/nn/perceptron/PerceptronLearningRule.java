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
package geym.nn.perceptron;

import java.io.Serializable;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.learning.SupervisedLearning;

public class PerceptronLearningRule extends SupervisedLearning implements Serializable {


    private static final long serialVersionUID = 2L;


    public PerceptronLearningRule() {

    }


    /**
     * This method implements the weights update procedure for the whole network
     * for the given output error vector.
     *   
     * @param outputError
     *            output error vector for some network input- the difference between desired and actual output
     * 
     * @see SupervisedLearning#calculateOutputError(double[], double[]) 
     * @see SupervisedLearning#learnPattern(org.neuroph.core.learning.SupervisedTrainingElement)  learnPattern
     */
    @Override
    protected void updateNetworkWeights(double[] outputError) {
        int i = 0;
        for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
            neuron.setError(outputError[i]); 
            double neuronError = neuron.getError();
            // 根据所有的神经元输入 迭代学习
            for (Connection connection : neuron.getInputConnections()) {
                // 神经元的一个输入
                double input = connection.getInput();
                // 计算权值的变更
                double weightChange =  neuronError * input;
                // 更新权值
                Weight weight = connection.getWeight();
                weight.weightChange = weightChange;                
                weight.value += weightChange;
            }

            i++;
        }
    }
}