package geym.nn.mnist;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.core.data.DataSet;

import geym.nn.util.Utils;

public class MnistReader {

	public static void unzipFileIfNotExist(String imgFile) throws IOException{
		File fImgFile=new File(imgFile);
		if(!fImgFile.exists()){
			Utils.decompress(imgFile+".gz");
		}
	}
    /**
     * 
     * @param imgFile
     * @param indexBefore
     *            get all images before indexBefore
     * @return
     * @throws IOException
     */
    public static BufferedImage[] readImg(String imgFile, int indexBefore) throws IOException {
    	unzipFileIfNotExist(imgFile);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(imgFile))));
        try {
            int magic = dis.readInt();
            if (magic != 0x00000803) {
                throw new RuntimeException("not a mnist imagin file");
            }
            int numberOfImg = dis.readInt();
            if (indexBefore > numberOfImg) {
                throw new RuntimeException("max number of image is " + numberOfImg);
            }
            int rows = dis.readInt();
            int cols = dis.readInt();
            BufferedImage[] re = new BufferedImage[indexBefore];
            byte[] bImg = new byte[rows * cols];
            for (int i = 0; i < indexBefore; i++) {
                dis.read(bImg);
                re[i] = createImgByBytes(bImg, rows, cols);
            }
            return re;
        } finally {
            dis.close();
        }
    }

    /**
     * 
     * @param bImg color for pixel
     * @param rows
     * @param cols
     * @return
     */
    public static BufferedImage createImgByBytes(byte[] bImg, int rows, int cols) {
        if (cols * rows != bImg.length) {
            throw new RuntimeException("image length error");
        }
        BufferedImage bi = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_RGB);
        for (int p = 0; p < bImg.length; p++) {
            bImg[p] = (byte) (255 - bImg[p]);
            bi.setRGB(p % rows, p / rows, (bImg[p] << 16) | (bImg[p] << 8) | (bImg[p]));
        }
        return bi;
    }

    /**
     * 
     * @param bImg  only 1 and 0
     * @param rows
     * @param cols
     * @return
     */
    public static BufferedImage createImgByBytes2(byte[] bImg, int rows, int cols) {
        if (cols * rows != bImg.length) {
            throw new RuntimeException("image length error");
        }
        BufferedImage bi = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_RGB);
        for (int p = 0; p < bImg.length; p++) {
            if (bImg[p] == 1)
                bImg[p] = (byte) 0xff;
            bi.setRGB(p % rows, p / rows, (bImg[p] << 16) | (bImg[p] << 8) | (bImg[p]));
        }
        return bi;
    }

    public static List<byte[]> readImgAsBytes(String imgFile, int indexBefore) throws IOException {
    	unzipFileIfNotExist(imgFile);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(imgFile))));
        try {
            int magic = dis.readInt();
            if (magic != 0x00000803) {
                throw new RuntimeException("not a mnist imagin file");
            }
            int numberOfImg = dis.readInt();
            if (indexBefore > numberOfImg) {
                throw new RuntimeException("max number of image is " + numberOfImg);
            }
            int rows = dis.readInt();
            int cols = dis.readInt();
            List<byte[]> re = new ArrayList<byte[]>(indexBefore);
            for (int i = 0; i < indexBefore; i++) {
                byte[] bImg = new byte[rows * cols];
                dis.read(bImg);
                for (int k = 0; k < bImg.length; k++) {
                    if ((bImg[k] & 0xff) < 128) {
                        bImg[k] = 0;
                    } else {
                        bImg[k] = 1;
                    }
                }
                re.add(bImg);
            }
            return re;
        } finally {
            dis.close();
        }
    }

    public static byte[] readImgLabel(String imgLabelFile, int indexBefore) throws IOException {
    	unzipFileIfNotExist(imgLabelFile);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(imgLabelFile))));
        try {
            int magic = dis.readInt();
            if (magic != 0x00000801) {
                throw new RuntimeException("not a mnist imagin label file");
            }
            int numberOfImg = dis.readInt();
            if (indexBefore > numberOfImg) {
                throw new RuntimeException("max number of image is " + numberOfImg);
            }
            byte[] lables = new byte[indexBefore];
            dis.read(lables);
            return lables;
        } finally {
            dis.close();
        }
    }

    public static DataSet trainingData(String imgFile, String imgLabelFile, int indexBefore) throws IOException {
        List<byte[]> inputs = readImgAsBytes(imgFile, indexBefore);
        byte[] labels = readImgLabel(imgLabelFile, indexBefore);
        DataSet ds = new DataSet(inputs.get(0).length, 10);
        for (int i = 0; i < inputs.size(); i++) {
            ds.addRow(encodeInput(inputs.get(i)), encodeOutput(labels[i]));
        }
        return ds;
    }

    private static double[] encodeInput(byte[] bs) {
        double[] re = new double[bs.length];
        for (int i = 0; i < bs.length; i++) {
            re[i] = bs[i];
        }
        return re;
    }
    
    private static double[] encodeInput2(byte[] bs) {
        double[] re = new double[bs.length];
        for (int i = 0; i < bs.length; i++) {
            re[i] = bs[i]&0xff;
        }
        return re;
    }

    private static double[] encodeOutput(byte b) {
        return NumberOutput.numbers.get(b);
    }

}
