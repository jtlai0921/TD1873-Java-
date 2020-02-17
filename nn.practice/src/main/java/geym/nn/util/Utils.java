package geym.nn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public class Utils {
	/**
	 * 将活跃度最高的
	 * 
	 * @param d
	 * @return
	 */
	public static double[] competition(double[] d) {
		double[] output = d;
		double[] re = new double[output.length];
		int maxIndex = 0;
		double maxValue = Double.MIN_VALUE;
		for (int i = 0; i < output.length; i++) {
			if (output[i] > maxValue) {
				maxIndex = i;
				maxValue = output[i];
			}
		}
		for (int i = 0; i < re.length; i++) {
			if (i == maxIndex) {
				re[i] = 1;
			} else {
				re[i] = 0;
			}
		}
		return re;
	}

	private static void decompress(InputStream is, OutputStream os) throws IOException {
		GZIPInputStream gis = new GZIPInputStream(is);
		try{
			int BUFFER = 512;
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = gis.read(data, 0, BUFFER)) != -1) {
				os.write(data, 0, count);
			}
		}finally{
			os.close();
			gis.close();
			is.close();
		}
	}
	
	public static void decompress(String gzFile) throws IOException {
		int i=gzFile.lastIndexOf('.');
		String outputFileName=gzFile.substring(0,i);
		decompress(new BufferedInputStream(new FileInputStream(gzFile)),
				new BufferedOutputStream(new FileOutputStream(outputFileName)));
	}
}
