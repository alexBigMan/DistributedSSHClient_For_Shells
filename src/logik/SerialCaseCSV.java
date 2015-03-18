package logik;

/*
 * Target of SerialCaseCSV
 * This class execute all the commands from the commandFile.
 * The difference between SerialCase and SerialCaseCSV is the 
 * possibility to vary the parameter of the options. 
 * Warning: All commands which use the same option will be changed   
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sshtools.ssh.SshIOException;

public class SerialCaseCSV extends SerialCase implements IListener{

	private int errorCodeFromRemoteCMD;

	public SerialCaseCSV(String username, String host, String commandFile) {
		super(username, host, commandFile);
		errorCodeFromRemoteCMD = 0;
	}

	@Override
	public int run() {
		int errorCode = 0;
		try {
			FileWriter fileWriter = new FileWriter("ConnectionShell_"
					+ connection.getHostname() + "_" + connection.getUsername()
					+ ".log");
			OutputStream connectedOutputTerminal = session.getOutputStream();
			InputStream hostResponses = session.getInputStream();
			CommandReaderCSV cmdReader = new CommandReaderCSV(new File(
					commandFile), this);
			ReaderThread readerThread = new ReaderThread(hostResponses, connectedOutputTerminal,
					fileWriter, connection.getUsername()+"@"+connection.getUsername());
			readerThread.register(this);
			readerThread.start();
			Thread.sleep(1000);
			ICommand command;
			boolean initCmdWasSend;
			while (cmdReader.hasNext()) {
				command = cmdReader.getCommand();
				initCmdWasSend = false;
				while (command.varyOptions() || !initCmdWasSend) {
					while (!readerThread.isReadyForCommand()) {
						Thread.sleep(100);
					}
					fileWriter.write("\t----ErrorCode for last command = "+ errorCodeFromRemoteCMD);
					fileWriter.write("\nCommand | " + command.execCommand(connectedOutputTerminal));
					initCmdWasSend = true;
				}
			}

			Thread.sleep(1000);
			readerThread.interrupt();
			connectedOutputTerminal.write("exit\n".getBytes());
			readerThread.join();
		} catch (SshIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return errorCode;
	}

	@Override
	public void update(int errorCode) {
		this.errorCodeFromRemoteCMD = errorCode;
	}

}
