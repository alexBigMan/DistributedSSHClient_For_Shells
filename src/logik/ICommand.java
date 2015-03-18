package logik;

import java.io.OutputStream;

public interface ICommand {
	String execCommand(OutputStream output);

	boolean varyOptions();
}
