package logik;

public class OptToVaryInteger implements IOptToVary {
	private String pathOpt;
	private String opt;
	private Integer begin;
	private Integer end;
	private Integer step;
	private int counter;

	public OptToVaryInteger(String pathOpt, String opt, Integer begin,
			Integer end, Integer step) {
		this(opt,begin,end,step);
		this.pathOpt = pathOpt;
	}

	public OptToVaryInteger(String opt, Integer begin, Integer end, Integer step) {
		this.opt = opt;
		this.pathOpt = "";
		this.begin = begin;
		this.end = end;
		this.step = step;
		counter = 0;
	}

	public String getOpt() {
		return opt;
	}

	public String getPath() {
		return pathOpt;
	}

	public String getNext() {
		Integer value = begin + (step * counter);
		counter++;
		return value.toString();
	}

	public void reset() {
		counter = 0;
	}

	public boolean hasNext() {
		boolean res = (begin + (step * counter) > end) ? false : true;
		return res;
	}
	
	public String toString(){
		return pathOpt + " | " + opt  + " | " + begin + " | " + end  + " | " + step;
	}
}
