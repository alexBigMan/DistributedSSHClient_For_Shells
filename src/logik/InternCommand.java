package logik;

import java.io.OutputStream;

public abstract class InternCommand implements ICommand{

	@Override
	abstract public String execCommand(OutputStream output);


}
