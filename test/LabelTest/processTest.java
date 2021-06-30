package LabelTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Assist.Process;

class processTest {

	@Test
	public void processGetTest() {
		Process p = new Process(2, "Java", 5, 10);
		assertEquals(2, p.getPid());
		assertEquals("Java", p.getName());
		assertEquals(5, p.getMinimumTime());
		assertEquals(10, p.getMaximumTime());
	}
	
	@Test
	public void courseEqualTest() {
		Process c0 = new Process(1, "Java", 5, 10);
		Process c1 = new Process(1, "Java", 5, 10);
		Process c2 = new Process(2, "Java", 5, 10);
		Process c3 = new Process(3, "C", 20, 100);
		
		assertTrue(c1.equals(c0));
		assertTrue(c0.equals(c1));
		
		assertFalse(c0.equals(c2));
		assertFalse(c0.equals(c3));
		assertFalse(c2.equals(c3));
		
		assertFalse(c0.equals(null));
	}
	
	@Test
	public void courseHashCodeTest() {
		Process c0 = new Process(1, "Java", 5, 10);
		assertEquals(1, c0.hashCode());
	}
	
	@Test
	public void courseToStringTest() {
		Process c0 = new Process(1, "Java", 5, 10);
		String res = "[Process1:Java,minTime:5,maxTime:10]";
		assertEquals(res, c0.toString());
	}


}
