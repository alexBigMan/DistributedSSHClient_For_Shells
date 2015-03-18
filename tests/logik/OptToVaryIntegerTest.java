package logik;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

public class OptToVaryIntegerTest {

	private OptToVaryInteger testee;
	
	@Before
	public void setUp() throws Exception {
		testee = new OptToVaryInteger("-p","Star", 0, 5, 2);
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
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("0"));
	}
	

	@Test
	public void testGetNext_second() {
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("2"));
	}

	@Test
	public void testGetNext_End() {
		testee.getNext();
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("4"));
	}
	
	@Test
	public void testGetNext_AfterEndReached() {
		testee.getNext();
		testee.getNext();
		testee.getNext();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("6"));
	}

	@Test
	public void testHasNext() {
		Assert.assertTrue(testee.hasNext());
	}
	
	@Test
	public void testHasNext_AfterEndReached() {
		testee.getNext();
		testee.getNext();
		testee.getNext();
		Assert.assertFalse(testee.hasNext());
	}
	
	@Test
	public void testGetNext_AfterReset() {
		testee.getNext();
		testee.reset();
		MatcherAssert.assertThat(testee.getNext(), Matchers.is("0"));
	}
	
	@Test
	public void testToString() {
		MatcherAssert.assertThat(testee.toString(), Matchers.is("-p | Star | 0 | 5 | 2"));
	}
	

}
