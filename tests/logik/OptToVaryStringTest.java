package logik;

import static org.junit.Assert.*;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptToVaryStringTest {

	private OptToVaryString testee;
	
	@Before
	public void setUp() throws Exception {
		testee = new OptToVaryString("-p","Star", Arrays.asList("Jupitar", "Mond", "Erde"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOpt() {
		MatcherAssert.assertThat(testee.getOpt(), Matchers.is("Star"));
	}
	
	@Test
	public void testGetPath() {
		MatcherAssert.assertThat(testee.getPath(), Matchers.is("-p"));
	}

	@Test
	public void testGetNext_first() {
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("Jupitar"));
	}
	

	@Test
	public void testGetNext_second() {
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("Mond"));
	}

	@Test
	public void testGetNext_End() {
		testee.getNext();
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("Erde"));
	}
	
	@Test
	public void testGetNext_AfterEndReached() {
		testee.getNext();
		testee.getNext();
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is(""));
	}

	@Test
	public void testHasNext_AfterEndReached() {
		Assert.assertTrue(testee.hasNext());
		testee.getNext();
		Assert.assertTrue(testee.hasNext());
		testee.getNext();
		Assert.assertTrue(testee.hasNext());
		testee.getNext();
		Assert.assertFalse(testee.hasNext());
	}

	@Test
	public void testHasNext() {
		Assert.assertTrue(testee.hasNext());
	}
	
	@Test
	public void testGetNext_AfterReset() {
		testee.getNext();
		testee.reset();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("Jupitar"));
	}
	
	@Test
	public void testToString() {
		MatcherAssert.assertThat(testee.toString(), Matchers.is("-p | Star | Jupitar | Mond | Erde"));
	}
	
}
