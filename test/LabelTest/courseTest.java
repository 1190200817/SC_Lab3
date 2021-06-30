package LabelTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Assist.Course;

class courseTest {

	@Test
	public void courseGetTest() {
		Course c = new Course(1, "SC", "WZJ", "ZX42");
		assertEquals(1, c.getCid());
		assertEquals("SC", c.getCourseName());
		assertEquals("WZJ", c.getTeacherName());
		assertEquals("ZX42", c.getLocation());
	}
	
	@Test
	public void courseEqualTest() {
		Course c0 = new Course(1, "SC", "WZJ", "ZX42");
		Course c1 = new Course(1, "SC", "WZJ", "ZX42");
		Course c2 = new Course(2, "CSAPP", "LHW", "ZX21");
		Course c3 = new Course(1, "SC", "LM", "ZX23");
		assertTrue(c1.equals(c0));
		assertTrue(c0.equals(c1));
		
		assertFalse(c0.equals(c2));
		assertFalse(c0.equals(c3));
		assertFalse(c2.equals(c3));
		
		assertFalse(c0.equals(null));
	}
	
	@Test
	public void courseHashCodeTest() {
		Course c0 = new Course(1, "SC", "WZJ", "ZX42");
		int res = "SC".hashCode() + "WZJ".hashCode() + "ZX42".hashCode() + 1;
		assertEquals(res, c0.hashCode());
	}
	
	@Test
	public void courseToStringTest() {
		Course c0 = new Course(1, "SC", "WZJ", "ZX42");
		String res = "[Course1:SC,Teacher:WZJ,Place:ZX42]";
		assertEquals(res, c0.toString());
	}
	
	@Test
	public void courseCompareTest() {
		Course c0 = new Course(1, "SC", "WZJ", "ZX42");
		Course c00 = new Course(1, "SC", "WZJ", "ZX42");
		Course c1 = new Course(1, "SC", "LM", "ZX42");
		Course c2 = new Course(2, "CSAPP", "LHW", "ZX21");
		Course c3 = new Course(1, "AC", "WZJ", "ZX42");
		assertEquals(true, c0.compareTo(c1) > 0);
		assertEquals(true, c1.compareTo(c2) < 0);
		assertEquals(true, c2.compareTo(c1) > 0);
		assertEquals(true, c0.compareTo(c00) == 0);
		assertEquals(true, c0.compareTo(c3) > 0);
	}

}
