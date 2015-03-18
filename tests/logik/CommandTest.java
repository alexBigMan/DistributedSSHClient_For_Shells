package logik;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import org.mockito.Matchers;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetPath() {
		Command testee = new Command("", "", new HashMap<String, HashMap<String,String>>(), new OptManipulator()); 
		testee.setPath("\\root\\home\\");
		String path = testee.getPath();
		assertThat(path, containsString("\\root\\home\\"));
	}
	
	@Test
	public void testSetCmd() {
		Command testee = new Command("", "", new HashMap<String, HashMap<String,String>>(), new OptManipulator()); 
		testee.setCmd("whois");
		String cmd = testee.getCmd();
		assertThat(cmd, containsString("whois"));
	}
	
	@Test
	public void testSetOptionOne(){
		Command testee = new Command("", "", new HashMap<String, HashMap<String,String>>(), new OptManipulator()); 
		testee.setOpt("-r", new HashMap<String,String>());
		String res = testee.toString();
		assertThat(res, containsString("-r"));
	}
	
	@Test 
	public void TestVarOptionsTrue(){
		IOptManipulator mManipulator = mock(IOptManipulator.class);
		@SuppressWarnings("unchecked")
		HashMap<String, HashMap<String,String>> mMap = mock(HashMap.class);
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		when(mMap.size()).thenReturn(1);
		when(mManipulator.hasNext()).thenReturn(true);
		when(mManipulator.getNext(Matchers.<HashMap<String, HashMap<String,String>>>any())).thenReturn(mMap);
		Command testee = new Command("","", map, mManipulator);
		assertThat(testee.varyOptions(), is(true));
	}
	
	@Test 
	public void TestVarOptionsFalse(){
		IOptManipulator mManipulator = mock(IOptManipulator.class);
		when(mManipulator.hasNext()).thenReturn(false);
		Command testee = new Command("","", null, mManipulator);
		assertThat(testee.varyOptions(), is(false));
	}
	
	@Test 
	public void TestVarOptionsFalseButHasNext(){
		IOptManipulator mManipulator = mock(IOptManipulator.class);
		@SuppressWarnings("unchecked")
		HashMap<String, HashMap<String,String>> mMap = mock(HashMap.class);
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		when(mMap.size()).thenReturn(0);
		when(mManipulator.hasNext()).thenReturn(true);
		when(mManipulator.getNext(Matchers.<HashMap<String, HashMap<String,String>>>any())).thenReturn(mMap);
		Command testee = new Command("","", map, mManipulator);
		assertThat(testee.varyOptions(), is(false));
	}
	
	@Test
	public void TestSetOpt(){
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		Command testee = new Command("","", map, null);
		HashMap<String, String> exMap = new HashMap<String, String>();
		testee.setOpt("-c", exMap);
		map = testee.getOpt();
		assertThat(map.get("-c"), is(exMap));
	}

	@Test
	public void TestExecCommandOneOption(){
		OutputStream mOutput = mock(OutputStream.class);
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		map.put("-c", new HashMap<String, String>());
		Command testee = new Command("path","cmd", map, null);
		testee.execCommand(mOutput);
		try {
			verify(mOutput, times(1)).write("path cmd -c;\n".getBytes());
			verify(mOutput, times(1)).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestExecCommandTwoOption(){
		OutputStream mOutput = mock(OutputStream.class);
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		HashMap<String, String> exMap = new HashMap<String, String>();
		exMap.put("Amount", "");
		exMap.put("Range", null);
		map.put("-c", exMap);
		Command testee = new Command("path","cmd", map, null);
		testee.execCommand(mOutput);
		try {
			verify(mOutput, times(1)).write("path cmd -c Amount Range;\n".getBytes());
			verify(mOutput, times(1)).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestExecCommandThreeOption(){
		OutputStream mOutput = mock(OutputStream.class);
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		HashMap<String, String> exMap = new HashMap<String, String>();
		exMap.put("Amount", "1000");
		exMap.put("Range", "\"831 AND 1000\"");
		map.put("-c", exMap);
		Command testee = new Command("","cmd", map, null);
		testee.execCommand(mOutput);
		try {
			verify(mOutput, times(1)).write("cmd -c Amount=1000 Range=\"831 AND 1000\";\n".getBytes());
			verify(mOutput, times(1)).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestExecCommandIOException(){
		OutputStream mOutput = mock(OutputStream.class);
		try {
			doThrow(IOException.class).when(mOutput).write("path cmd\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Command testee = new Command("path","cmd", new HashMap<String, HashMap<String, String>>(), new OptManipulator());
		testee.execCommand(mOutput);
	}
	
	@Test
	public void TestToString(){
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
		HashMap<String, String> exMap = new HashMap<String, String>();
		exMap.put("Amount", "");
		exMap.put("Range", "\"831 AND 1000\"");
		map.put("-c", exMap);
		Command testee = new Command("path","cmd", map, null);
		assertThat(testee.toString(), equalTo("path cmd Option :-c Value :Amount Option :-c exOption :Range Value :\"831 AND 1000\""));
	}
}
