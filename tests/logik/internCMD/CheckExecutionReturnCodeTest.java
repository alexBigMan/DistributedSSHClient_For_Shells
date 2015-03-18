package logik.internCMD;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckExecutionReturnCodeTest {

		CheckExecutionReturnCode testee;
		FileWriter fw;
		RandomAccessFile fr;
		
		@Before
		public void setUp(){
			String file = "ConnectionShell_testUser_127.0.0.1.log";
			try {
				fw = new FileWriter(file);
				fr = new RandomAccessFile(file, "r");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			OutputStream os = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
				}
			};
			testee = new CheckExecutionReturnCode(os, fr);
		}

		@Test
		public void testGetErrorCodeFromLastExecCmd(){
			try {
				fw.write("ls -l halle/bla \t\n [userTest@userTest] echo $? \t\n 127");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int errorCodeFromLastExecCmd = testee.getErrorCodeFromLastExecCmd();
			Assert.assertTrue(errorCodeFromLastExecCmd == 127);
		}
		

		@Test 
		public void testGetErrorCodeFromLastExecCmdWithMoreNumbers(){
			try {
				fw.write("ls -l halle/bla \t\n [userTest@userTest] echo $? \t\n 110 ab 127");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int errorCodeFromLastExecCmd = testee.getErrorCodeFromLastExecCmd();
			Assert.assertTrue(errorCodeFromLastExecCmd == Integer.MIN_VALUE);
		}
		
}
