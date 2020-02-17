package geym.nn.som.yearbook;

import java.util.Arrays;

public class CityInfo {
	public static int DATA_COL_NUM = 0; 
	
	private String city;
	private double[] x = new double[DATA_COL_NUM];

	public CityInfo(String city) {
		super();
		this.city = city;
	}

	public void setValue(int i, double value) {
		x[i] = value;
	}

	public double getValue(int i) {
		return x[i];
	}

	public String getCtiy() {
		return city;
	}

	@Override
	public String toString() {
		return "CityInfo [city=" + city + ", x=" + Arrays.toString(x) + "]";
	}
	
	public int columnSize(){
		return x.length;
	}
	
	
}
