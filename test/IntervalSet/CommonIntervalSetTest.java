package IntervalSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * Tests for RepMapIntervalSet.
 * 
 * This class runs the IntervalSetTest tests against RepMapIntervalSet, as
 * well as tests for that particular implementation.
 * 
 * Tests against the IntervalSet spec should be in RepMapIntervalSet.
 */

public class CommonIntervalSetTest extends IntervalSetTest {
	/*
	 * Provide a RepListIntervalSet for tests in IntervalSetTest.
	 */
	
	@Override
	public IntervalSet<String> emptyInstance() {
		return new CommonIntervalSet<String>();
	}

	/*
	 * Testing RepMapIntervalSet...
	 */

	// Testing strategy for CommonIntervalSet.toString()
	// 添加一些时间段，测试toString是否正确输出

	// TODO tests for CommonIntervalSet.toString()
	@Test
	public void toStringTest() {
		IntervalSet<String> inset = emptyInstance();
		try {
			inset.insert(0, 3, "abc");
			inset.insert(2, 4, "bcd");
			inset.insert(1, 5, "def");
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals("bcd,time:[2:4);abc,time:[0:3);def,time:[1:5);", inset.toString());
		inset.remove("bcd");
		assertEquals("abc,time:[0:3);def,time:[1:5);", inset.toString());
		
		
	}
}
