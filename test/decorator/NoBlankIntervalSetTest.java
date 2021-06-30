package decorator;

import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import Assist.Employee;
import Assist.Position;
import Assist.time;
import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class NoBlankIntervalSetTest {

	// NoBlankIntervalSet是接口，只需要测试接口中仅有的方法——blankIntervals
	@Test
	public void blankTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		NoBlankIntervalSet<String> nbi = new CommonNoBlankIntervalSet<>(0, 10, inv);
		assertEquals(0, nbi.getStartTime());
		assertEquals(10, nbi.getEndTime());
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "cde";
		String s4 = "def";
		
		// 切为两段
		try {
			inv.insert(3, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		Set<time> timeSet = new HashSet<time>();
		timeSet.add(new time(0, 3));
		timeSet.add(new time(5, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		try {
			inv.insert(5, 10, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		inv.remove(s2);
		timeSet.add(new time(5, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		// 切为右半段
		try {
			inv.insert(5, 9, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 10));
		timeSet.add(new time(9, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		inv.remove(s2);
		timeSet.add(new time(5, 10));
		timeSet.remove(new time(9, 10));
		
		// 切为左半段
		try {
			inv.insert(8, 10, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 10));
		timeSet.add(new time(5, 8));
		assertEquals(timeSet, nbi.blankIntervals());
		inv.remove(s2);
		timeSet.add(new time(5, 10));
		timeSet.remove(new time(5, 8));
		assertEquals(timeSet, nbi.blankIntervals());
		
		try {
			inv.insert(0, 3, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(0, 3));
		assertEquals(timeSet, nbi.blankIntervals());
		
		inv.remove(s2);
		timeSet.add(new time(0, 3));
		assertEquals(timeSet, nbi.blankIntervals());
		
		try {
			inv.insert(1, 2, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(0, 3));
		timeSet.add(new time(0, 1));
		timeSet.add(new time(2, 3));
		assertEquals(timeSet, nbi.blankIntervals());
		
		try {
			inv.insert(6, 8, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		timeSet.remove(new time(5, 10));
		timeSet.add(new time(5, 6));
		timeSet.add(new time(8, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		// 存在Overlap
		try {
			inv.insert(5, 10, s4);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 6));
		timeSet.remove(new time(8, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		inv.remove(s4);
		timeSet.add(new time(5, 6));
		timeSet.add(new time(8, 10));
		assertEquals(timeSet, nbi.blankIntervals());
		
		try {
			inv.insert(0, 10, s4);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		timeSet.remove(new time(5, 6));
		timeSet.remove(new time(8, 10));
		timeSet.remove(new time(0, 1));
		timeSet.remove(new time(2, 3));
		assertEquals(timeSet, nbi.blankIntervals());
		
 	}
	
	 @Test
    public void copyTest() {
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	Employee e2 = new Employee("王", Position.SECRETARY, 123456);
    	Employee e3 = new Employee("张", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = new CommonNoBlankIntervalSet<Employee>(1,10,IntervalSet.empty());
    	try {
			inset.insert(1, 2, e1);
			inset.insert(2, 4, e2);
			inset.insert(3, 4, e3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	IntervalSet<Employee> insetCopy = inset.copy();
    	// copy的任何结果相同
    	assertEquals(inset.labels(), insetCopy.labels());
    	assertEquals(inset.start(e2), insetCopy.start(e2));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.remove(e3), insetCopy.remove(e3));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.labels(), insetCopy.labels());
    }

}
