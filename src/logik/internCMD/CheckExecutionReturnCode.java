package logik.internCMD;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logik.ICommand;

import org.apache.log4j.Logger;

public class CheckExecutionReturnCode{

	private OutputStream os;
	private RandomAccessFile fr;

	public CheckExecutionReturnCode(OutputStream os){
		this.os = os;
	}
	
	public CheckExecutionReturnCode(OutputStream os, RandomAccessFile fr ){
		this(os);
		this.fr = fr;
	}
	
	public int getErrorCodeFromLastExecCmd(){
		return getErrorCodeFromLastExecCmd(os, fr);
	}
	
	static public int getErrorCodeFromLastExecCmd(OutputStream os, RandomAccessFile fr){
		StringBuilder sb = new StringBuilder();
		try {
			sendReturnCmdForLastExecCmd(os);
			long filePointer = fr.length() -1;
			int symNewLine;
			Thread.sleep(1000); //wait for response
			fr.seek(filePointer);
			while((symNewLine = fr.read()) != '\n'){
				sb.append((char) symNewLine);
				fr.seek(--filePointer);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		String returnCode = sb.reverse().toString();
		Pattern patterNumber = Pattern.compile("(\\d+)");
		Matcher matcher = patterNumber.matcher(returnCode);
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
	
	static private void sendReturnCmdForLastExecCmd(OutputStream os) throws IOException{
		os.write("echo $?;".getBytes());
	}
	
}
