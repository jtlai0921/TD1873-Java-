package geym.nn.som.yearbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class YearBookReader {
	private List<String> cityList = new ArrayList<String>();

	public YearBookReader() throws IOException {
		BufferedReader fr = null;
		try {
			fr = new BufferedReader(new InputStreamReader(YearBookReader.class.getResourceAsStream("CityList.txt")));
			String line = null;
			while ((line = fr.readLine()) != null) {
				cityList.add(line.trim());
			}
		} finally {
			fr.close();
		}
	}

	public double readNumber(Cell cell) {
		if (cell == null) {
			System.out.println("单元格为空");
			return 0;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		} else {
			return Double.parseDouble(cell.getStringCellValue().trim());
		}
	}

	public String readString(Cell cell) {
		if (cell == null) {
			return "";
		}
		return cell.getStringCellValue().trim();
	}

	public String readContent(File file, int number) {
		StringBuffer sb = new StringBuffer();
		FileInputStream fileInputStream = null;
		HSSFWorkbook workbook = null;
		try {
			fileInputStream = new FileInputStream(file);
			workbook = new HSSFWorkbook(fileInputStream);
			int sheetCount = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				HSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					String cityName = readString(row.getCell(0)).replace("市", "");
					if (!"".equals(cityName) && cityList.contains(cityName)) {
						double value = readNumber(row.getCell(2));
						CityInfoList.instance().set(cityName, number, value);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public void readCityInfoList() {
		File yearbookDir = new File("src/main/resources/yearbook/");
		File[] files = yearbookDir.listFiles();
		CityInfo.DATA_COL_NUM=files.length;
		System.out.println("属性个数："+CityInfo.DATA_COL_NUM);
		for (int i = 0; i < files.length; i++) {
			readContent(files[i], i);
		}
		System.out.println(CityInfoList.instance().toFile());
	}

}
