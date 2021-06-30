package Specific;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import decorator.NoOverlapIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class ProcessIntervalSetTest {

	// 主要测试ProcessIntervalSet是否符合三个维度上的特性
	
	// NoOverlap测试
	@Test
	void noOverlapTest() {
		ProcessIntervalSet<String> inv = new ProcessIntervalSet<String>();
		String s1 = "abc";
		String s2 = "bcd";
		
	    boolean flag = false;
		try {
			inv.insert(3, 5, s1);
			inv.insert(4, 6, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
	// 非周期性 和 允许空白 无需测试

	// 初始化测试
	@Test
	public void initTest() {
		IntervalSet<String> ins = new NoOverlapIntervalSet<String>(IntervalSet.empty());
		String s1 = "abc";
		String s2 = "bcd";
		try {
			ins.insert(3, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		ProcessIntervalSet<String> inv = new ProcessIntervalSet<String>(ins);
		
		boolean flag = false;
		try {
			inv.insert(4, 6, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
	}
}
