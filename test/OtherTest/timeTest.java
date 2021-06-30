package OtherTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Assist.time;

class timeTest {

	@Test
	public void timeGetTest() {
		time c = new time(1, 3);
		assertEquals(1, c.getStart());
		assertEquals(3, c.getEnd());
	}
	
	@Test
	public void courseEqualTest() {
		time c0 = new time(1, 3);
		time c1 = new time(1, 3);
		time c2 = new time(1, 5);
		time c3 = new time(0, 3);
		time c4 = new time(0, 4);

		assertTrue(c1.equals(c0));
		assertTrue(c0.equals(c1));
		
		assertFalse(c0.equals(c2));
		assertFalse(c0.equals(c3));
		assertFalse(c0.equals(c4));
		assertFalse(c2.equals(c3));
		assertFalse(c2.equals(c4));
		assertFalse(c4.equals(c3));
		
		assertFalse(c0.equals(null));
	}
	
	@Test
	public void courseHashCodeTest() {
		time c0 = new time(1, 3);
		assertEquals(4, c0.hashCode());
	}
	
	@Test
	public void courseToStringTest() {
		time c0 = new time(1, 3);
		String res = "time:[1:3)";
		assertEquals(res, c0.toString());
	}
	
	@Test
	public void courseCompareTest() {
		time c0 = new time(1, 4);
		time c1 = new time(2, 3);
		time c2 = new time(0, 4);
		time c3 = new time(1, 3);
		
		assertEquals(-1, c0.compareTo(c1));
		assertEquals(1, c0.compareTo(c2));
		assertEquals(0, c0.compareTo(c3));
		assertEquals(1, c1.compareTo(c0));
		assertEquals(-1, c2.compareTo(c3));
		
	}

}
