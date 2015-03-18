package logik;

import java.util.HashMap;


public class DummyCommand extends Command {

	public DummyCommand() {
		super("dummy", "dummy", new HashMap<String, HashMap<String,String>>(0), null);
	}

}
