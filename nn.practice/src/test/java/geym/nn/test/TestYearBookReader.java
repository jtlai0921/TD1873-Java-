package geym.nn.test;

import geym.nn.som.yearbook.YearBookReader;

import java.io.IOException;

import org.junit.Test;

public class TestYearBookReader {
	
	@Test
	public void test() throws IOException{
		YearBookReader reader=new YearBookReader();
		reader.readCityInfoList();
		
	}
}
