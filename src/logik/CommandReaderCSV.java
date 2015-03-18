package logik;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

public class CommandReaderCSV implements IFileCommandReader {

	private File file;
	private Logger logger = Logger.getLogger(CommandReaderCSV.class);

	private Iterator<CSVRecord> iterParser;
	protected CSVParser parser;
	static public CSVFormat format = CSVFormat.EXCEL.withDelimiter(';').withQuote(null);
	private IExecutionCase exCase;

	public CommandReaderCSV(File file, IExecutionCase exCase) {
		this.file = file;
		this.exCase = exCase;
	}

	@Override
	public void setFile(File file) {
		try {
			if(parser != null)
			{
				parser.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		parser = null;
		iterParser = null;
		this.file = file;
	}

	@Override
	public ICommand getCommand() throws InputMismatchException {
		ICommand command = new DummyCommand();
		String cmd = "";
		String path = "";
		HashMap<String, HashMap<String, String>> opt = new LinkedHashMap<String, HashMap<String, String>>(
				15);
		int counterRecords = 0;
		IOptManipulator manipulator = new OptManipulator();
		if (hasNext()) {
			Iterator<String> it = iterParser.next().iterator();
			
			while (it.hasNext()) {
				String value = it.next();
				
				if (value.startsWith("--")){
					command = createInternCommand(value);
					return command;
				}
				else if (value.startsWith("#")) {
					optionsToVary(opt, manipulator, value);
					//extended options are optional
					continue;
				} else if (counterRecords == 0) {
					path = value;
				} else if (counterRecords == 1) {
					cmd = value;
				} else{
					insertOptions(opt, value);
				}
				counterRecords++;
			}

			command = new Command(path, cmd, opt, manipulator);
		}
		return command;
	}

	private void optionsToVary(HashMap<String, HashMap<String, String>> opt, IOptManipulator manipulator, String value) {
		IOptToVary optionToVary = createOptToVary(value);
		if (optionToVary != null) {
			manipulator.setOptions(opt, optionToVary);
		}
	}

	private void insertOptions(HashMap<String, HashMap<String, String>> opt,
			String value) {
		String[] patterns = new String[]{"(([\\w=]*\"(\\w+|\\s)+\")|([[^\"\\s]\\S]+))","(\"[\\w+\\W]+\")","(.+)"};//{"(\\w+)=\"(\\w+(\\W\\w+)*)\"","(\\w+)=(\\w+)","(\"[\\w+\\W]+\")","([\\w+\\W]+)"};
		ArrayList<String> values = new ArrayList<String>();
		boolean found = false;
		Pattern pattern;
		for(String p : patterns){
			pattern = Pattern.compile(p);
			Matcher matcher = pattern.matcher(value);
			while (matcher.find()) {
				values.add(matcher.group(1));
				found = true;
			}
			if(found) break;
		}
		HashMap<String, String> extOpt = new LinkedHashMap<String, String>(
				5);
		if (values.size() == 1) {
			opt.put(values.get(0), extOpt);
		} else if (values.size() == 2) {
			extOpt.put(values.get(1), "");
			opt.put(values.get(0), extOpt);
		} else {
			String[] exParam;
			for (int i = 1; i < values.size(); i++) {
				exParam = values.get(i).split("=");
				if (exParam.length != 2)
					throw new InputMismatchException(
							"Unknown shell input format");
				extOpt.put(exParam[0], exParam[1]);
			}
			if(values.size() > 0){
				opt.put(values.get(0), extOpt);
			}
		}
	}

	private ICommand createInternCommand(String value) {
		return InternCommandFactory.getInternCommand(value, exCase);
	}

	private IOptToVary createOptToVary(String value) {
		Matcher m = Pattern
				.compile(
						"(#[\\s]*(\\w+)\\x5B([\"\\w+\",]*)(\"\\w+\")\\x5D)|(#[\\s]*(-.)[\\s](\\w+)\\x5B(\\d+),(\\d+),(\\d+)\\x5D)|(#[\\s]*(\\w+)\\x5B(\\d+),(\\d+),(\\d+)\\x5D)|(#[\\s]*(-.)[\\s](\\w+)\\x5B([\"\\w+\",]*)(\"\\w+\")\\x5D)")
				.matcher(value);

		IOptToVary optionsToVary;
		while (m.find()) {
			if (m.group(1) != null) {
				ArrayList<String> range = new ArrayList<String>();
				String[] tmp = m.group(3).split(",");
				for (int i = 0; i < tmp.length; i++) {
					range.add(tmp[i]);
				}
				range.add(m.group(4));
				optionsToVary = new OptToVaryString(m.group(2), range);
				return optionsToVary;
			}
			if (m.group(5) != null) {
				optionsToVary = new OptToVaryInteger(m.group(6), m.group(7),
						Integer.parseInt(m.group(8)), Integer.parseInt(m
								.group(9)), Integer.parseInt(m.group(10)));
				return optionsToVary;
			}
			if (m.group(11) != null) {
				optionsToVary = new OptToVaryInteger(m.group(12),
						Integer.parseInt(m.group(13)), Integer.parseInt(m
								.group(14)), Integer.parseInt(m.group(15)));
				return optionsToVary;
			}
			if (m.group(16) != null) {
				ArrayList<String> range = new ArrayList<String>();
				String[] tmp = m.group(19).split(",");
				for (int i = 0; i < tmp.length; i++) {
					range.add(tmp[i]);
				}
				range.add(m.group(20));
				optionsToVary = new OptToVaryString(m.group(17), m.group(18), range);
				return optionsToVary;
			}
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		if (parser == null) {
			try {
				parser = CSVParser
						.parse(file, Charset.forName("UTF-8"), format);
			} catch (IOException e) {
				e.printStackTrace();
			}
			iterParser = parser.iterator();
		}

		boolean res = iterParser.hasNext();
		if (!res) {
			try {
				parser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

}
