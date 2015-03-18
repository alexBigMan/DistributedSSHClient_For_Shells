package logik;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.sshtools.ssh.SshIOException;

import logik.internCMD.CheckExecutionReturnCode;
import logik.internCMD.PathCMD;

public class InternCommandFactory {

	static Pattern returnCode = Pattern.compile("--RETURN_CODE");
	static Pattern path = Pattern.compile("--PATH\\s(\\\\|/)?((\\w+\\\\)+)|((\\w+/)+)");
	static Logger logger = Logger.getLogger(InternCommandFactory.class);
	static private ICommand dummy = new ICommand() {
		@Override
		public String execCommand(OutputStream output) {
			return "dummy in InternCommandFactory.getInternCommand";
		}
		@Override
		public boolean varyOptions() {
			return false;
		}
	};
	
	static public ICommand getInternCommand(String value, IExecutionCase exCase) {
		ICommand cmd = dummy;
		/*
		 * In Java.util.regex.Matcher exist a bug. The find-Method send true back but the matches-Method send false.
		 */
		if (path.matcher(value).find()) {
			cmd = createPathCMD(path.matcher(value));
		}
// See Method createReturnCodeCMD
//		if (returnCode.matcher(value).matches()) {
//			try {
//				cmd = createReturnCodeCMD(exCase.getSession().getOutputStream());
//			} catch (FileNotFoundException | SshIOException e) {
//				logger.debug("InternCommandFactory.getInternCommand : " + e.getMessage());
//			}
//		}
		return cmd;
	}

	static private ICommand createPathCMD(Matcher matcher) {
		ICommand cmd;
		String sPath;
		matcher.find();
		sPath = matcher.group(1);
		boolean isRoot = false;
		if (sPath != null) {
			isRoot = true;
		}
		if((sPath = matcher.group(2)) != null || (sPath = matcher.group(4)) != null){
			cmd = new PathCMD(sPath.split("/|\\\\"), isRoot);
		}else{
			cmd = dummy;
		}
		return cmd;
	}
//	Can be deleted. Im not sure if this part is useable
//
//	static private ICommand createReturnCodeCMD(OutputStream os)
//			throws FileNotFoundException {
//		FileAppender appender = (FileAppender) Logger.getRootLogger()
//				.getAppender("FILE");
//		RandomAccessFile fr = new RandomAccessFile(appender.getFile(), "r");
//		return new CheckExecutionReturnCode(os, fr);
//	}
}
