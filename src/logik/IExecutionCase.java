package logik;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.List;

import com.sshtools.ssh.SshSession;

public interface IExecutionCase {
	
	List<Connection> getConnectionInfo();

	Reader getCommandFile() throws FileNotFoundException;
	
	SshSession getSession();
	
	void setSession(SshSession session);
	
	void closeSession();

	int run();


}
