package geym.nn.isolet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import geym.nn.util.Utils;

public class IsoletReader {

	public static void unzipFileIfNotExist(String trainFile) throws IOException {
		File fImgFile = new File(trainFile);
		if (!fImgFile.exists()) {
			Utils.decompress(trainFile + ".gz");
		}
	}

	public static DataSet trainingData(String trainFile) throws FileNotFoundException, IOException {
		unzipFileIfNotExist(trainFile);
		DataSet ds = new DataSet(617, 26);
		File fTrainFile = new File(trainFile);
		BufferedReader br = new BufferedReader(new FileReader(fTrainFile));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] strNumber=line.substring(0, line.length()-1).split(",");
			double[] input = new double[strNumber.length-1];
			double[] output = new double[26];
			for(int i=0;i<strNumber.length-1;i++){
				input[i]=Double.parseDouble(strNumber[i].trim());
			}
			output[Integer.parseInt(strNumber[strNumber.length-1].trim())-1]=1;
			DataSetRow dr=new DataSetRow(input,output);
			ds.addRow(dr);
		}
		return ds;
	}
	
	

}
