package logik;

import java.util.HashMap;

public interface IOptManipulator {
	boolean hasNext();
	void setOptions(HashMap<String, HashMap<String, String>> opt, IOptToVary option);
	HashMap<String, HashMap<String, String>> getNext(
			HashMap<String, HashMap<String, String>> opt);
}
