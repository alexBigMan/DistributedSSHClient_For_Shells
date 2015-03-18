package logik;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logik.internCMD.CheckExecutionReturnCode;

import org.apache.log4j.Logger;

public abstract class AbstractRunCase implements IExecutionCase {

	private Logger logger = Logger.getLogger(AbstractRunCase.class.getName());

	public abstract int run();

	public void stopReader(OutputStream sendCmd, ReaderThread readerThread)
			throws IOException, InterruptedException {
		readerThread.interrupt();
		sendCmd.write("exit\n".getBytes());
		readerThread.join();
	}

	protected class ReaderThread extends Thread implements IInformant{
		private InputStream hostResponses;
		private FileWriter responseWriter;
		private AtomicBoolean readyForCommand;
		private CharSequence commandLinePromptIdentifier;
		private OutputStream sendCmd;
		private List<IListener> listener = new ArrayList<IListener>();
		public ReaderThread(InputStream hostResponses, OutputStream sendCmd,
				FileWriter responseWriter, String commandLinePromptIdentifier) {
			this.hostResponses = hostResponses;
			this.sendCmd = sendCmd;
			this.responseWriter = responseWriter;
			readyForCommand = new AtomicBoolean(false);
			this.commandLinePromptIdentifier = commandLinePromptIdentifier;
		}

		@Override
		public void run() {
			int sym;
			boolean isInterrupted = false;
			StringBuffer outputString = new StringBuffer();
			boolean requestForReturnCodeSend = false;
			try {
				while ((sym = hostResponses.read()) > -1
						&& !isInterrupted) {
					if(Thread.interrupted()){
						isInterrupted = true;
						hostResponses.close();
					}
					
					if(sym == 0x08){
						outputString.delete(outputString.length()-1, outputString.length());
					}else{
						outputString.append((char) sym);
					}
					if (outputString.toString().contains(
							commandLinePromptIdentifier)) {
						String output = outputString.toString();
						responseWriter.write(output);
						logger.info(output);
						if(!requestForReturnCodeSend){
							sendReturnCodeCmdForLastExecCmd(sendCmd);
							requestForReturnCodeSend = true;
						}else{
							informListener(readCodeFromOutputString(output));
							readyForCommand.set(true);
							requestForReturnCodeSend = false;
						}
						outputString = new StringBuffer();
					}
				}
				responseWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private int readCodeFromOutputString(String output) {
			Pattern pattern = Pattern.compile("(\\d+)\\W+"+commandLinePromptIdentifier);
			Matcher matcher = pattern.matcher(output.toString());
			int errorCode = Integer.MIN_VALUE;
			while(matcher.find()){
				if(errorCode != Integer.MIN_VALUE){
					Logger.getLogger(CheckExecutionReturnCode.class).error("there exists more than one error code in last line");
					return Integer.MIN_VALUE; 
				}
				errorCode = Integer.parseInt(matcher.group(1));
			}
			return errorCode;
		}

		private void sendReturnCodeCmdForLastExecCmd(OutputStream os) throws IOException{
			os.write("echo $?\n".getBytes());
		}
		
		public boolean isReadyForCommand() {
			if (readyForCommand.get()) {
				readyForCommand.set(false);
				return true;
			}

			return false;
		}

		private void informListener(int errorCode){
			for(IListener l : listener){
				l.update(errorCode);
			}
		}
		
		@Override
		public void register(IListener l) {
			listener.add(l);
		}

		@Override
		public void unregister(IListener l) {
			listener.remove(l);
		}
	}
}
