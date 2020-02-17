package geym.nn.som;

import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.norm.VectorNormalizer;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.KohonenCos;
import org.neuroph.util.random.CenterVectorWeight;
import org.neuroph.util.random.NormalizeWeight;

public class KohonenAnimal {
	public static final int mapSize = 4;
	public static double[][] data = {
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0 }, // 鸽子
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 母鸡
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1 }, // 鸭
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1 }, // 鹅
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0 }, // 猫头鹰
			{ 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0 }, // 凖
			{ 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0 }, // 鹰
			{ 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0 }, // 狐狸
			{ 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0 }, // 狗
			{ 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0 }, // 狼
			{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0 }, // 猫
			{ 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0 }, // 虎
			{ 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0 }, // 狮
			{ 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0 }, // 马
			{ 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0 }, // 斑马
			{ 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 } // 牛
	};

	public static String[] dataKey = { "鸽子", "母鸡", "鸭", "鹅", "猫头鹰", "凖", "鹰", "狐狸", "狗", "狼", "猫", "虎", "狮", "马", "斑马",
			"牛" };

	public static void main(String[] args) {
		Kohonen som = new KohonenCos(13, mapSize * mapSize);
		DataSet ds = new DataSet(13);
		for (double[] row : data) {
			ds.addRow(new DataSetRow(row));
		}
		som.learn(ds);
		
		ResultFrame frame = new ResultFrame();
		for (int i = 0; i < data.length; i++) {
			som.setInput(data[i]);
			som.calculate();
			int winnerIndex = getWinnerIndex(som);
			int x = getRowFromIndex(winnerIndex);
			int y = getColFromIndex(winnerIndex);
			System.out.println(dataKey[i] + " " + x + " " + y);
			frame.addElementString(frame.new ElementString(dataKey[i], x, y));
		}
		frame.showMe();
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
}
