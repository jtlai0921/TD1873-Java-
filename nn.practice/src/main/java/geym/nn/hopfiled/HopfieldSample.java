package geym.nn.hopfiled;

import java.util.Arrays;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.comp.neuron.InputOutputNeuron;
import org.neuroph.nnet.learning.HopfieldLearning;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

import geym.nn.adaline.AdalineDemo;

public class HopfieldSample {
    public static String[][] DIGITS = { 
            { " OOO ",
              "O   O",
              "O   O",
              "O   O",
              "O   O",
              "O   O",
              " OOO "  },

            { "  O  ",
              " OO  ",
              "O O  ",
              "  O  ",
              "  O  ",
              "  O  ",
              "  O  "  }
    };
    
    public static String[][] BAD_DIGITS = { 
            { " OOO ",
              "O   O",
              "O  OO",
              "O    ",
              "OOOOO",
              "OOOOO",
              " OOO "  },

            { "  O  ",
              " OO  ",
              "O O  ",
              "    O",
              "     ",
              "     ",
              "     "  }
    };
    
	public static void main(String args[]) {
		DataSet trainingSet = new DataSet(35);
		trainingSet.addRow(AdalineDemo.createTrainDataRow(DIGITS[0], 0)); // 0
		trainingSet.addRow(AdalineDemo.createTrainDataRow(DIGITS[1], 0)); // 1
		
		Hopfield myHopfield = new Hopfield(35);
		myHopfield.learn(trainingSet);
		
		System.out.println("Testing network");
		for (int i = 0; i <= 1; i++) {
			recallDigit(myHopfield, BAD_DIGITS[i]);
		}
	}

	private static void recallDigit( Hopfield myHopfield, String[] bad_digit) {
		DataSetRow h = AdalineDemo.createTrainDataRow(bad_digit, 0); 		
		myHopfield.setInput(h.getInput());
		double[] networkOutput = null;
		double[] preNetworkOutput = null;
		while (true) {
			myHopfield.calculate();
			networkOutput = myHopfield.getOutput();
			if (preNetworkOutput == null) {
				preNetworkOutput = networkOutput;
				continue;
			}
			if (Arrays.equals(networkOutput, preNetworkOutput)) {
				break;
			}
			preNetworkOutput = networkOutput;
		}

		System.out.println("Input: ");
		printDigit(h.getInput());
		System.out.println(" Output ====>" );
		printDigit(networkOutput);
	}
	
	public static void printDigit(double[] networkOutput){
	    for(int i=0;i<networkOutput.length;i++){
	        if(networkOutput[i]>0)
	            System.out.print("O");
	        else
	            System.out.print(" ");
	        if((i+1)%5==0)System.out.println();
	    }
	}
}
