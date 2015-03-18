package logik;

import java.util.List;

import com.sshtools.ssh.SshSession;

public interface IController {
	List<SshSession> connect(IExecutionCase exCase);
	void disconnect(IExecutionCase exCase);
	void disconnectAll();
	int run(IExecutionCase exCase);
	int run();
	void mergeFiles(IExecutionCase exCase);
	
}
