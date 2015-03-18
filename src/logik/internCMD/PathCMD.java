package logik.internCMD;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import logik.InternCommand;

/**
 * 
 * @author alex This class creates a path. On error you'll get an error message
 *         in the log file.
 */

public class PathCMD extends InternCommand {

	private String[] path;
	private boolean rootPath;

	public PathCMD(String[] path, boolean rootPath) {
		this.path = path;
		this.rootPath = rootPath;
	}

	@Override
	public String execCommand(OutputStream output) {
		StringBuilder p = new StringBuilder();
		if(rootPath){
			p.append("/");
		}
		
		for(String s : path){
			if(s.equalsIgnoreCase("")){
				continue;
			}
			p.append("\""+s+"\"/");
		}
		String ret = "mkdir -p "+ p.toString()+"\n";
		try {
			output.write(ret.getBytes());
		} catch (IOException e) {
			Logger.getLogger(PathCMD.class).error(e.getMessage());
		}
		return ret;
	}

	@Override
	public boolean varyOptions() {
		return false;
	}
	
	public boolean isRootPath(){
		return rootPath;
	}

}
