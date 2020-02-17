package org.neuroph.core.data.norm;

import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class VectorNormalizer implements Normalizer {

	@Override
	public void normalize(DataSet dataSet) {
		List<DataSetRow> rows = dataSet.getRows();
		for(DataSetRow row:rows){
			normalize(row);
		}
	}
	
	public void normalize(DataSetRow row) {
		double sum = 0;
		for (int i = 0; i < row.getInput().length; i++) {
			sum += row.getInput()[i] * row.getInput()[i];
		}
		sum=Math.sqrt(sum);
		for (int i = 0; i < row.getInput().length; i++) {
			row.getInput()[i]=row.getInput()[i]/sum;
		}
	}

}
