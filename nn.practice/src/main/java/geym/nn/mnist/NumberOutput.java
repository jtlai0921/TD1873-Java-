package geym.nn.mnist;

import java.util.ArrayList;
import java.util.List;

public class NumberOutput {
	public static List<double[]> numbers=new ArrayList<double[]>(10);
	static{
		for(int i=0;i<10;i++){
			double[] tmp=new double[10];
			tmp[i]=1;
			numbers.add(tmp);
		}
	}
}
