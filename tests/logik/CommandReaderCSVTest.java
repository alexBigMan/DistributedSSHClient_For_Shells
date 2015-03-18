package logik;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import com.sshtools.ssh.SshSession;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

public class CommandReaderCSVTest {

	private class testCaseDummy implements IExecutionCase{

		@Override
		public List<Connection> getConnectionInfo() {
			return null;
		}

		@Override
		public Reader getCommandFile() throws FileNotFoundException {
			return null;
		}

		@Override
		public SshSession getSession() {
			return null;
		}

		@Override
		public void setSession(SshSession session) {
		}

		@Override
		public void closeSession() {
		}

		@Override
		public int run() {
			return 0;
		}
		
	}
	
	private class CommandReaderCSVTestDummy extends CommandReaderCSV {

		public CommandReaderCSVTestDummy(File file) {
			super(file, new testCaseDummy());
		}

		public void setParser(CSVParser parser) {
			this.parser = parser;
		}

	}

	// @Test
	// public void testGetCommand() {
	// if (testee.hasNext()) {
	// command1();
	// }
	// }
	//
	// private void command1() {
	// Command cmd = (Command)testee.getCommand();
	// assertEquals(cmd.getPath(), "");
	// assertEquals(cmd.getCmd(), "etlclient.bat");
	// HashMap<String, HashMap<String, String>> opt = cmd.getOpt();
	// StringBuilder str = new StringBuilder();
	// Set<String> keySet = opt.keySet();
	// for (String key : keySet) {
	// HashMap<String, String> extOpt = opt.get(key);
	// if (extOpt.isEmpty()) {
	// str.append(" " + key);
	// } else {
	// Set<String> keySet2 = extOpt.keySet();
	// str.append(" " + key);
	//
	// for (String str2 : keySet2) {
	// String string = extOpt.get(str2);
	// if (string == null || string.equalsIgnoreCase("")) {
	// str.append(" " + str2);
	// } else {
	// str.append(" " + str2 + "=" + string);
	// }
	// }
	// }
	// }
	// System.out.println(str.toString());
	// String st = str.toString();
	// assertEquals(
	// " -o parallelLogs\\CubeLoad\\full\\1000\\6.log -p wikitests -c Range=\"831 AND 1000\" Amount=\"1000\" -j CubeLoadParallel",
	// st);
	// }
	//
	// private void command2() {
	// Command cmd = (Command)testee.getCommand();
	// assertEquals(cmd.getPath(), "");
	// assertEquals(cmd.getCmd(), "cd");
	// HashMap<String, HashMap<String, String>> opt = cmd.getOpt();
	// Set<String> keySet = opt.keySet();
	// assertEquals(keySet.iterator().next(), "/opt/jedox/ps");
	// }
	//
	// @Test
	// public void testGetCommand2() {
	// if (testee.hasNext()) {
	// command1();
	// command2();
	// }
	// }

	@Test
	public void testSetFile() {
		CommandReaderCSV testee = new CommandReaderCSV(new File(
				"tests\\CommandReaderCSV_testFile.csv"), new testCaseDummy());
		testee.setFile(new File("tests\\CommandReaderCSV_testFile.csv"));
	}

	@Test
	public void testSetFileAndClose() {
		CommandReaderCSV testee = new CommandReaderCSV(new File(
				"tests\\CommandReaderCSV_testFile.csv"),new testCaseDummy());
		testee.hasNext();
		testee.setFile(new File("tests\\CommandReaderCSV_testFile.csv"));
	}

	@Test
	public void testHasNextTrue() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\CommandReaderCSV_testFile.csv"));
		assertThat(testee.hasNext(), is(true));
	}

	@Test
	public void testHasNextFalse() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\CommandReaderCSV_testFile.csv"));
		for (int i = 0; i < 5; i++) {
			testee.hasNext();
			testee.getCommand();
		}
		assertThat(testee.hasNext(), is(false));
	}

	@Test
	public void testGetCommandEmptyFile() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\emptyTest.csv"));
		ICommand cmd = testee.getCommand();
		assertThat(cmd, instanceOf(DummyCommand.class));
	}

	@Test
	public void testGetCommandOneCmd() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\CommandReaderCSV_testFile.csv"));
		OutputStream mOutput = mock(OutputStream.class);
		String strCMD = "etlclient.bat -p wikitests -j CubeLoadParallel -c Range=\"Fisch\" Amount=0 Range \"Auge\" Amount 10 -o parallelLogs\\CubeLoad\\full\\1000\\6.log;\n";
		ICommand cmd = testee.getCommand();
		assertThat(cmd.execCommand(mOutput), equalTo(strCMD));

		try {
			verify(mOutput, times(1)).write(strCMD.getBytes());
			verify(mOutput, times(1)).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetCommandWithExtendedOption() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\CommandReaderCSV_testFileExtendedOption.csv"));
		OutputStream mOutput = mock(OutputStream.class);
		String strCMD = "etlclient.bat -c Range=\"831 AND 1000\" Amount=\"1000\";\n";
		ICommand cmd = testee.getCommand();
		assertThat(cmd.execCommand(mOutput), equalTo(strCMD));
	}

	@Test(expected = InputMismatchException.class)
	public void testGetCommandInputMismatchException() {
		CommandReaderCSVTestDummy testee = new CommandReaderCSVTestDummy(
				new File("tests\\inputMismatch.csv"));
		testee.hasNext();
		testee.getCommand();
	}
}
