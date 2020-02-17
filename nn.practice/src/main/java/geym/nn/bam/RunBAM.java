package geym.nn.bam;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class RunBAM {
	public static final String[] NAMES = { "TINA ", "ANTJE", "LISA " };
	public static final String[] PHONES = { "6843726", "8034673", "7260915" };

	public static final String[] NAMES_BAD = { "TINE ", "ANNJE", "LISE " };
	public static final String[] PHONES_BAD = { "6843725", "8034679", "7260905" };

	public static final int IN_CHARS = 5;
	public static final int OUT_CHARS = 7;
	public static final int BITS_PER_CHAR = 6;
	public static final char FIRST_CHAR = ' ';
	public static final int INPUT_NEURONS = (IN_CHARS * BITS_PER_CHAR);
	public static final int OUTPUT_NEURONS = (OUT_CHARS * BITS_PER_CHAR);

	public static double[] stringToBipolar(String str) {
		double[] result = new double[str.length() * BITS_PER_CHAR];
		int currentIndex = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = Character.toUpperCase(str.charAt(i));
			int idx = ch - FIRST_CHAR;

			int place = 1;
			for (int j = 0; j < BITS_PER_CHAR; j++) {
				boolean value = (idx & place) > 0;
				result[currentIndex++] = value ? 1 : -1;
				place <<= 1;
			}
		}
		return result;
	}

	public static String bipolalToString(double[] data) {
		StringBuilder result = new StringBuilder();

		int j, a, p;

		for (int i = 0; i < (data.length / BITS_PER_CHAR); i++) {
			a = 0;
			p = 1;
			for (j = 0; j < BITS_PER_CHAR; j++) {
				if (data[(i * BITS_PER_CHAR + j)] > 0)
					a += p;
				p <<= 1;
			}
			result.append((char) (a + FIRST_CHAR));
		}
		return result.toString();
	}

	public static double[] randomBiPolar(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			if (Math.random() > 0.5)
				result[i] = -1;
			else
				result[i] = 1;
		}
		return result;
	}

	public static String mappingToString(DataSetRow row) {
		StringBuilder result = new StringBuilder();
		result.append(bipolalToString(row.getInput()));
		result.append(" -> ");
		result.append(bipolalToString(row.getDesiredOutput()));
		return result.toString();
	}

	public static void runBAM(BAM logic, DataSetRow data) {
		StringBuilder line = new StringBuilder();
		line.append(mappingToString(data));
		logic.calculate(data);
		line.append("  |  ");
		line.append(mappingToString(data));
		System.out.println(line.toString());
	}

	public static void main(String[] args) {
		BAM logic = new BAM(INPUT_NEURONS, OUTPUT_NEURONS);
		DataSet ds = new DataSet(INPUT_NEURONS, OUTPUT_NEURONS);
		// train
		for (int i = 0; i < NAMES.length; i++) {
			DataSetRow dr = new DataSetRow();
			dr.setInput(stringToBipolar(NAMES[i]));
			dr.setDesiredOutput(stringToBipolar(PHONES[i]));
			ds.addRow(dr);
		}

		logic.learn(ds);

		for (int i = 0; i < NAMES.length; i++) {
			DataSetRow dr = new DataSetRow();
			dr.setInput(stringToBipolar(NAMES[i]));
			dr.setDesiredOutput(randomBiPolar(OUT_CHARS * BITS_PER_CHAR));
			runBAM(logic, dr);
		}

		System.out.println();

		for (int i = 0; i < NAMES_BAD.length; i++) {
			DataSetRow dr = new DataSetRow();
			dr.setInput(stringToBipolar(NAMES_BAD[i]));
			dr.setDesiredOutput(randomBiPolar(OUT_CHARS * BITS_PER_CHAR));
			runBAM(logic, dr);
		}

		logic.switchInputOutput();
		System.out.println();
		for (int i = 0; i < PHONES.length; i++) {
			DataSetRow dr = new DataSetRow();
			dr.setInput(stringToBipolar(PHONES[i]));
			dr.setDesiredOutput(randomBiPolar(IN_CHARS * BITS_PER_CHAR));
			runBAM(logic, dr);
		}

		System.out.println();
		for (int i = 0; i < PHONES_BAD.length; i++) {
			DataSetRow dr = new DataSetRow();
			dr.setInput(stringToBipolar(PHONES_BAD[i]));
			dr.setDesiredOutput(randomBiPolar(IN_CHARS * BITS_PER_CHAR));
			runBAM(logic, dr);
		}
	}
}
