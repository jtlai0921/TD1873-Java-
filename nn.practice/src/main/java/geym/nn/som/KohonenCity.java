package geym.nn.som;

import java.io.IOException;
import java.util.List;

import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.norm.RangeNormalizer;
import org.neuroph.core.data.norm.VectorNormalizer;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.KohonenCos;
import org.neuroph.util.random.CenterVectorWeight;
import org.neuroph.util.random.NormalizeWeight;

import geym.nn.som.yearbook.CityInfo;
import geym.nn.som.yearbook.CityInfoList;
import geym.nn.som.yearbook.YearBookReader;

public class KohonenCity {
	public static final int mapSize=10;
	static double[][] data = null;
	static DataSet ds = null;
	
	public static String[] dataKey = null;

	public static void readDataFromXls() throws IOException{
		YearBookReader ybr=new YearBookReader();
		ybr.readCityInfoList();
	}
	
	public static void initData() throws IOException {
		readDataFromXls();
		List<String> allLine = readDataFromFile();
		dataKey = new String[allLine.size()];
		data = new double[allLine.size()][CityInfo.DATA_COL_NUM];
		int i = 0;
		for (String line : allLine) {
			if(line==null)continue;
			String[] cols = line.trim().split(" ");
			if (cols.length != CityInfo.DATA_COL_NUM+1){
				throw new RuntimeException("file format error" + i);
			}
			dataKey[i] = cols[0];
			for (int j = 1; j < CityInfo.DATA_COL_NUM+1; j++) {
				data[i][j - 1] = Double.parseDouble(cols[j]);
			}
			i++;
		}
		
		ds=new DataSet(CityInfo.DATA_COL_NUM);
		for (double[] row : data) {
			ds.addRow(new DataSetRow(row));
		}
		
	}

	public static List<String> readDataFromFile() throws IOException {
		return CityInfoList.instance().toList();
	}

	public static void normalizeData() {		
		new RangeNormalizer(0,1).normalize(ds);
		System.out.println(ds);
	}

	// get unit with closetst weight vector
	private static int getWinnerIndex(Kohonen neuralNetwork) {
		Neuron winner = new Neuron();
		double minOutput = 100;
		int winnerIndex = -1;
		Neuron[] neurons = neuralNetwork.getLayerAt(1).getNeurons();
		for (int i = 0; i < neurons.length; i++) {
			double out = neurons[i].getOutput();
			if (out < minOutput) {
				minOutput = out;
				winnerIndex = i;
			} // if
		} // while
		return winnerIndex;
	}

	/**
	 * 在二维平面中的坐标
	 * 
	 * @param index
	 * @return
	 */
	private static int getRowFromIndex(int index) {
		return index / mapSize + 1;
	}

	private static int getColFromIndex(int index) {
		return index % mapSize + 1;
	}
	
	private static Kohonen createKohonenAndLearn() {
		Kohonen som = new KohonenCos(CityInfo.DATA_COL_NUM, mapSize*mapSize);
		som.learn(ds);
		return som;
	}
	
	private static void paintCityAtSOM(Kohonen som) {
		ResultFrame frame = new ResultFrame();
		for (int i = 0; i < data.length; i++) {
			paintOneCity(som, frame, i);
		}
		frame.showMe();
	}

	private static void paintOneCity(Kohonen som, ResultFrame frame, int i) {
		som.setInput(ds.getRowAt(i).getInput());
		som.calculate();
		int winnerIndex = getWinnerIndex(som);
		int x = getRowFromIndex(winnerIndex);
		int y = getColFromIndex(winnerIndex);
		System.out.println(dataKey[i] + " " + x + " " + y);
		frame.addElementString(frame.new ElementString(dataKey[i], x, y));
	}
	
	
	public static void main(String[] args) throws IOException {
		initData();
		normalizeData();
		Kohonen som = createKohonenAndLearn();
		paintCityAtSOM(som);
	}
}
