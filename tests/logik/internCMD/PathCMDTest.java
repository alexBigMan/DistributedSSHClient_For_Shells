package logik.internCMD;

import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PathCMDTest {

	PathCMD testee;

	@Test
	public void testCreatePathRoot() {
		testee = new PathCMD("\\home\\zimadmin\\alex\\test\\".split("\\\\"),true);
		String execCommand = testee.execCommand(new OutputStream(){

			@Override
			public void write(int b) throws IOException {
			}
		});
		MatcherAssert.assertThat(execCommand, Matchers.is("mkdir -p /\"home\"/\"zimadmin\"/\"alex\"/\"test\"/\n"));
	}

	@Test
	public void testCreatePathNotRoot() {
		testee = new PathCMD("zimadmin\\alex\\test\\".split("\\\\"),false);
		String execCommand = testee.execCommand(new OutputStream(){

			@Override
			public void write(int b) throws IOException {
			}
		});
		MatcherAssert.assertThat(execCommand, Matchers.is("mkdir -p \"zimadmin\"/\"alex\"/\"test\"/\n"));
	}
	
	@Test
	public void testCreatePathNotRootSlash() {
		testee = new PathCMD("zimadmin/alex/test/".split("/"),false);
		String execCommand = testee.execCommand(new OutputStream(){

			@Override
			public void write(int b) throws IOException {
			}
		});
		MatcherAssert.assertThat(execCommand, Matchers.is("mkdir -p \"zimadmin\"/\"alex\"/\"test\"/\n"));
	}
	
	@Test
	public void testCreatePathRootSlash() {
		testee = new PathCMD("/home/zimadmin/alex/test/".split("/"),true);
		String execCommand = testee.execCommand(new OutputStream(){

			@Override
			public void write(int b) throws IOException {
			}
		});
		MatcherAssert.assertThat(execCommand, Matchers.is("mkdir -p /\"home\"/\"zimadmin\"/\"alex\"/\"test\"/\n"));
	}
}
