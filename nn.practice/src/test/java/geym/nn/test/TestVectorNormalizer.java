package geym.nn.test;

import java.util.Arrays;

import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.norm.VectorNormalizer;

public class TestVectorNormalizer {
	@Test
	public void test() {
		VectorNormalizer v = new VectorNormalizer();
		double[][] data = { { 1, 1, 1 } };
		DataSet ds = new DataSet(3);
		for (double[] row : data) {
			ds.addRow(new DataSetRow(row));
		}
		v.normalize(ds);
		System.out.println(Arrays.toString(data[0]));

	}
}
