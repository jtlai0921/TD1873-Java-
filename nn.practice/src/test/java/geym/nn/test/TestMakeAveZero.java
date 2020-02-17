package geym.nn.test;
import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import geym.nn.pca.MnistPca;

public class TestMakeAveZero {
	
	@Test
	public void test(){
		DataSet ds=new DataSet(3,0);
		ds.addRow(new DataSetRow(new double[]{1,0,1}));
		ds.addRow(new DataSetRow(new double[]{2,3,1}));
		ds.addRow(new DataSetRow(new double[]{0,1,1}));
		ds.addRow(new DataSetRow(new double[]{1,4,1}));
		
		new MnistPca().makeAveZero(ds);
		System.out.println(ds);
	}
}
