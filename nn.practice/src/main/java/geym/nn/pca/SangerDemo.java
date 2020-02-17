package geym.nn.pca;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;


public class SangerDemo  implements LearningEventListener{

	public static void main(String[] args) {
		new SangerDemo().run();
	}
	
    public void run() {
    	
        DataSet trainingSet = new DataSet(3, 2);
        trainingSet.addRow(new DataSetRow(new double[]{-1.5,0,0.5}, new double[]{0,0}));
        trainingSet.addRow(new DataSetRow(new double[]{-0.5,1,1.5}, new double[]{0,0}));
        trainingSet.addRow(new DataSetRow(new double[]{0.5,-1,-0.5}, new double[]{0,0}));
        trainingSet.addRow(new DataSetRow(new double[]{1.5,0,-1.5}, new double[]{0,0}));
        SangerNetwork sanger = new SangerNetwork(3,2);

        SangerLearning learningRule =(SangerLearning) sanger.getLearningRule();
        learningRule.setLearningRate(0.01);
        learningRule.addListener(this);
        System.out.println(Arrays.toString(sanger.getWeights()));
        
        // 进行学习
        System.out.println("Training neural network...");
        sanger.learn(trainingSet);

        System.out.println("Testing trained neural network");
        testNeuralNetwork(sanger, trainingSet);
    }

	   /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for(DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( testSetRow.getInput() ) );
            System.out.println("Output: " + Arrays.toString( networkOutput) );
        }
    }
    
    @Override
    public void handleLearningEvent(LearningEvent event) {
        IterativeLearning bp = (IterativeLearning)event.getSource();
        System.out.println("iterate:"+bp.getCurrentIteration()); 
        System.out.println(Arrays.toString(bp.getNeuralNetwork().getWeights()));
    }    

}
