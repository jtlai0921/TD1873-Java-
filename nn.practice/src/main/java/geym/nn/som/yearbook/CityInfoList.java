package geym.nn.som.yearbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityInfoList {
	private Map<String,CityInfo> cityinfos=new HashMap<String,CityInfo>();
	private static CityInfoList cil=new CityInfoList();
	private CityInfoList(){
		
	}
	
	public static CityInfoList instance(){
		return cil;
	}
	
	public void set(String cityName,int i,double value){
		CityInfo ci=cityinfos.get(cityName);
		if(ci==null){
			ci=new CityInfo(cityName);
			cityinfos.put(cityName, ci);
		}
		ci.setValue(i, value);
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		for(CityInfo ci:cityinfos.values()){
			sb.append(ci.toString()).append("\r\n");
		}
		return sb.toString();
	}
	
	public String toFile(){
		StringBuffer sb=new StringBuffer();
		for(CityInfo ci:cityinfos.values()){
			sb.append(ci.getCtiy()).append(" ");
			for(int k=0;k<ci.columnSize();k++){
				sb.append(ci.getValue(k)).append(" ");
			}
			
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	public List<String> toList(){
		List<String> re=new ArrayList<String>();
		for(CityInfo ci:cityinfos.values()){
			StringBuffer sb=new StringBuffer();
			sb.append(ci.getCtiy()).append(" ");
			for(int k=0;k<ci.columnSize();k++){
				sb.append(ci.getValue(k)).append(" ");
			}
			re.add(sb.toString());
		}
		return re;
	}
	
}
