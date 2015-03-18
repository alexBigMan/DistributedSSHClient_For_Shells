package logik;

import java.io.File;

public interface IFileCommandReader {
	void setFile(File file);
	ICommand getCommand();
	boolean hasNext();
}
