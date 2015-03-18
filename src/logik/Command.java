package logik;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import logik.internCMD.CheckExecutionReturnCode;

public class Command implements ICommand {

	private String path;
	private String cmd;
	private HashMap<String, HashMap<String, String>> opt;
	private IOptManipulator manipulator;

	public Command(String path, String cmd,
			HashMap<String, HashMap<String, String>> opt,
			IOptManipulator manipulator) {
		this.cmd = cmd;
		this.opt = opt;
		this.path = path;
		this.setManipulator(manipulator);
	}

	public String getPath() {
		return path;
	}

	protected void setPath(String path) {
		this.path = path;
	}

	public String getCmd() {
		return cmd;
	}

	protected void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public boolean varyOptions() {
		if (manipulator.hasNext()) {
			if (manipulator.getNext(opt).size() != 0) {
				return true;
			}
		}
		return false;
	}

	public HashMap<String, HashMap<String, String>> getOpt() {
		return opt;
	}

	protected void setOpt(String key, HashMap<String, String> opt) {
		this.opt.put(key, opt);
	}

	public String execCommand(OutputStream output) {
		StringBuilder str = new StringBuilder();
		if(path.equals("")){
			str.append(cmd);
		}else{
			str.append(path + " " + cmd);
		}			
		Set<String> keySet = opt.keySet();
		for (String key : keySet) {
			HashMap<String, String> extOpt = opt.get(key);
			if (extOpt.isEmpty()) {
				str.append(" " + key);
			} else {
				Set<String> keySet2 = extOpt.keySet();
				str.append(" " + key);

				for (String str2 : keySet2) {
					String string = extOpt.get(str2);
					if (string == null || string.equalsIgnoreCase("")) {
						str.append(" " + str2);
					} else {
						str.append(" " + str2 + "=" + string);
					}
				}
			}
		}
		str.append(";\n");
		String ret = str.toString();
		try {
			output.write(ret.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(path + " " + cmd);
		Set<String> keySet = opt.keySet();
		for (String key : keySet) {
			HashMap<String, String> extOpt = opt.get(key);
			if (extOpt.isEmpty()) {
				str.append(" Option :" + key);
			} else {
				Set<String> keySet2 = extOpt.keySet();
				for (String str2 : keySet2) {
					String string = extOpt.get(str2);
					if (string == null || string.equalsIgnoreCase("")) {
						str.append(" Option :" + key + " Value :" + str2);
					} else {
						str.append(" Option :" + key + " exOption :" + str2
								+ " Value :" + string);
					}
				}
			}
		}
		return str.toString();
	}

	public void setManipulator(IOptManipulator manipulator) {
		if (manipulator != null)
			this.manipulator = manipulator;
	}
}
