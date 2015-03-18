package logik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OptManipulator implements IOptManipulator{

	private List<IOptToVary> options;
	private int pointer;
	
	public OptManipulator(){
		options = new ArrayList<IOptToVary>();
	}
	
	@Override
	public HashMap<String, HashMap<String, String>> getNext(HashMap<String, HashMap<String, String>> opt) {
		return manipulateOpt(opt);
	}

	@Override
	public boolean hasNext() {
		for(IOptToVary option : options){
			if(option.hasNext()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void setOptions(HashMap<String, HashMap<String, String>> opt, IOptToVary option) {
		this.options.add(option);
		replaceValueAtOption(opt, option);
		pointer = options.size() - 1;
	}
	
	private HashMap<String, HashMap<String, String>> manipulateOpt(
			HashMap<String, HashMap<String, String>> options) {
		IOptToVary optionToVary = this.options.get(pointer);
		if (optionToVary.hasNext()) {
			if(!replaceValueAtOption(options, optionToVary)){
				// no elements changed because extend option not available 
				return new HashMap<String, HashMap<String, String>>();
			}
		} else {
			optionToVary.reset();
			if (pointer > 0) {
				pointer--;
				replaceValueAtOption(options, optionToVary);
				options = manipulateOpt(options);
				pointer++;
			} else {
				// it was iterated over all elements
				options =  new HashMap<String, HashMap<String, String>>();
			}
		}
		return options;
	}

	private boolean replaceValueAtOption(
			HashMap<String, HashMap<String, String>> opt, IOptToVary manipulator) {
		boolean hasChanged = false;
		if (!manipulator.getPath().equalsIgnoreCase("")) {
			HashMap<String, String> extOpt = opt.get(manipulator.getPath());
			if (extOpt != null) {
				extOpt.put(manipulator.getOpt(), manipulator.getNext());
				hasChanged = true;
			}
		} else {
			HashMap<String, String> newValue = new LinkedHashMap<String, String>();
			newValue.put(manipulator.getNext(), "");
			opt.put(manipulator.getOpt(), newValue);
			hasChanged = true;
		}
		
		return hasChanged;
	}
	
}
