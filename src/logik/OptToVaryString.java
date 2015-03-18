package logik;

import java.util.List;

public class OptToVaryString implements IOptToVary {
	private String pathOpt;
	private String opt;
	private List<String> range;
	private int counter;

	public OptToVaryString(String pathOpt, String opt, List<String> range) {
		this(opt,range);
		this.pathOpt = pathOpt;
	}

	public OptToVaryString(String opt,  List<String> range) {
		this.opt = opt;
		this.pathOpt = "";
		this.range = range;
		counter = 0;
	}

	public String getOpt() {
		return opt;
	}

	public String getPath() {
		return pathOpt;
	}

	public String getNext() {
		
		String value;
		if(counter >= range.size()){
			value = "";
		}else{
			value = range.get(counter);
			counter++;
		}
		return value.toString();
	}

	public void reset(){
		counter = 0;
	}
	
	public boolean hasNext() {
		boolean res = range.size() > counter ? true : false;
		return res;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(String s : range){
			builder.append(" | " + s);
		}
		return pathOpt + " | " + opt  + builder.toString();
	}
}
