package geym.nn.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import geym.nn.mnist.MnistReader;

public class TestMnistReader {

	public static final String MnistDir = "src\\main\\resources\\handswriter\\mnist";

	@Test
	public void testReadImg() throws IOException {
		BufferedImage[] bi = MnistReader.readImg(MnistDir + "\\train-images.idx3-ubyte", 10);
		for (int i = 0; i < bi.length; i++) {
			ImageIO.write(bi[i], "jpg", new File("d:\\temp\\img\\write" + i + ".jpg"));
		}
	}

	@Test
	public void testReadImgBytes() throws IOException {
		List<byte[]> bytes = MnistReader.readImgAsBytes(MnistDir + "\\train-images.idx3-ubyte", 20);
		byte[] labels = MnistReader.readImgLabel(MnistDir + "\\train-labels.idx1-ubyte", 20);
		for (int i = 0; i < bytes.size(); i++) {
			BufferedImage bi = MnistReader.createImgByBytes2(bytes.get(i), 28, 28);
			for(int k=0;k<bi.getWidth();k++){
				for(int j=0;j<bi.getHeight();j++){
					bi.setRGB(k, j, ~bi.getRGB(k, j));
				}
			}
			ImageIO.write(bi, "jpg", new File("d:\\temp\\img\\write" + labels[i] + "-" + i + ".jpg"));
		}
	}

	@Test
	public void testReadImgLabel() throws IOException {
		byte[] bytes = MnistReader.readImgLabel(MnistDir + "\\train-labels.idx1-ubyte", 10);
		System.out.println(Arrays.toString(bytes));
	}
	
	public static void main(String args[]) throws IOException{
	    new TestMnistReader().testReadImgBytes();
	}
}
