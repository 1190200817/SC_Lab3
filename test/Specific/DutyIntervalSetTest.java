package Specific;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import Assist.time;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class DutyIntervalSetTest {

	// 主要测试DutyIntervalSet是否符合三个维度上的特性
	
	// NoBlank测试
	@Test
	void noBlankTest() {
		DutyIntervalSet<String> inv = new DutyIntervalSet<String>(0, 10);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "cde";
		
		// 切为两段
		try {
			inv.insert(3, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		Set<time> timeSet = new HashSet<time>();
		timeSet.add(new time(0, 3));
		timeSet.add(new time(5, 10));
		assertEquals(timeSet, inv.blankIntervals());
		
		try {
			inv.insert(5, 10, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 10));
		assertEquals(timeSet, inv.blankIntervals());
		
		inv.remove(s2);
		timeSet.add(new time(5, 10));
		assertEquals(timeSet, inv.blankIntervals());
		
		// 切为右半段
		try {
			inv.insert(5, 9, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(5, 10));
		timeSet.add(new time(9, 10));
		assertEquals(timeSet, inv.blankIntervals());
		
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
		assertEquals(timeSet, inv.blankIntervals());
		inv.remove(s2);
		timeSet.add(new time(5, 10));
		timeSet.remove(new time(5, 8));
		assertEquals(timeSet, inv.blankIntervals());
		
		try {
			inv.insert(0, 3, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(0, 3));
		assertEquals(timeSet, inv.blankIntervals());
		
		inv.remove(s2);
		timeSet.add(new time(0, 3));
		assertEquals(timeSet, inv.blankIntervals());
		
		try {
			inv.insert(1, 2, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		timeSet.remove(new time(0, 3));
		timeSet.add(new time(0, 1));
		timeSet.add(new time(2, 3));
		assertEquals(timeSet, inv.blankIntervals());
		
		try {
			inv.insert(6, 8, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		timeSet.remove(new time(5, 10));
		timeSet.add(new time(5, 6));
		timeSet.add(new time(8, 10));
		assertEquals(timeSet, inv.blankIntervals());
	} 

	// NoOverlap测试
	@Test
	void noOverlapTest() {
		DutyIntervalSet<String> inv = new DutyIntervalSet<String>(0, 10);
		String s1 = "abc";
		
	    boolean flag = false;
		try {
			inv.insert(3, 5, s1);
			inv.insert(4, 6, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
}
