package Specific;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class CourseIntervalSetTest {

	// 主要测试CourseIntervalSet是否符合三个维度上的特性
	// 周期性测试
	@Test
	public void periodicTest() {
		long period = 11;
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>(period);
		
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 无周期插入成功
		try {
			cis.insert(1, 2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(1, cis.intervals(s1).labels().size());
		assertEquals(1, cis.intervals(s1).start(0));
		assertEquals(2, cis.intervals(s1).end(0));
		
		try {
			cis.insert(3, 6, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(2, cis.intervals(s1).labels().size());
		assertEquals(1, cis.intervals(s1).start(0));
		assertEquals(2, cis.intervals(s1).end(0));
		assertEquals(3, cis.intervals(s1).start(1));
		assertEquals(6, cis.intervals(s1).end(1));
		
		cis.remove(s1);
		assertEquals(0, cis.labels().size());
		
		// start >= end
		try {
			cis.insert(2, 1, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, cis.labels().size());
		
		// start < 0,成功
		try {
			cis.insert(-3, -2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(8, cis.intervals(s1).start(0));
		assertEquals(9, cis.intervals(s1).end(0));
		
		cis.remove(s1);
		// 跨越周期，且插入成功
		try {
			cis.insert(13, 16, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(2, cis.intervals(s1).start(0));
		assertEquals(5, cis.intervals(s1).end(0));
		
		// 跨越周期，但分为两半，插入成功
		cis.remove(s1);
		assertEquals(0, cis.labels().size());
		try {
			cis.insert(10, 12, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(2, cis.intervals(s1).labels().size());
		assertEquals(0, cis.intervals(s1).start(0));
		assertEquals(1, cis.intervals(s1).end(0));
		assertEquals(10, cis.intervals(s1).start(1));
		assertEquals(11, cis.intervals(s1).end(1));
		
		cis.remove(s1);
		// 重复插入相同时间段，不出错
		try {
			cis.insert(5, 9, s1);
			cis.insert(16, 20, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(5, cis.intervals(s1).start(0));
		assertEquals(9, cis.intervals(s1).end(0));
		
		// 重复插入不冲突时间段，不出错
		try {
			cis.insert(21, 22, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(2, cis.intervals(s1).labels().size());
		assertEquals(5, cis.intervals(s1).start(0));
		assertEquals(9, cis.intervals(s1).end(0));
		assertEquals(10, cis.intervals(s1).start(1));
		assertEquals(11, cis.intervals(s1).end(1));
		
		cis.remove(s1);
		
		// 重复插入冲突时间段，出错
		boolean flag = false;
		try {
			cis.insert(3, 7, s1);
			cis.insert(17, 19, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		
		assertTrue(flag);
		assertEquals(1, cis.labels().size());
		assertEquals(1, cis.intervals(s1).labels().size());
		assertEquals(3, cis.intervals(s1).start(0));
		assertEquals(7, cis.intervals(s1).end(0));
		cis.remove(s1);
		
		// 时间段长度大于period
		try {
			cis.insert(23, 35, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, cis.labels().size());
		
		// 时间段长度等于period
		try {
			cis.insert(23, 34, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(0, cis.intervals(s1).start(0));
		assertEquals(11, cis.intervals(s1).end(0));
		cis.remove(s1);
		// 跨越周期的多标签插入
		try {
			cis.insert(22, 24, s1);
			cis.insert(39, 42, s2);
			cis.insert(21, 24, s2);
			cis.insert(44, 55, s3);  // 跨越整个周期
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, cis.labels().size());
		assertEquals(0, cis.intervals(s1).start(0));
		assertEquals(2, cis.intervals(s1).end(0));
		assertEquals(0, cis.intervals(s2).start(0));
		assertEquals(2, cis.intervals(s2).end(0));
		assertEquals(6, cis.intervals(s2).start(1));
		assertEquals(9, cis.intervals(s2).end(1));
		assertEquals(10, cis.intervals(s2).start(2));
		assertEquals(11, cis.intervals(s2).end(2));
		assertEquals(0, cis.intervals(s3).start(0));
		assertEquals(11, cis.intervals(s3).end(0));	 
	}

	// Overlap测试
	@Test
	public void overlapTest() {
		long period = 11;
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>(period);
		
		String s1 = "abc";
		String s2 = "bcd";
		
		try {
			cis.insert(1, 2, s1);
			cis.insert(12, 13, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(2, cis.labels().size());
		assertEquals(1, cis.intervals(s1).start(0));
		assertEquals(1, cis.intervals(s2).start(0));
		assertEquals(2, cis.intervals(s1).end(0));
		assertEquals(2, cis.intervals(s2).end(0));
	}
	
	// 初始化测试
	@Test
	public void initTest() {
		IntervalSet<String> mis = IntervalSet.empty();
		String s1 = "abc";
		try {
			mis.insert(1, 2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		long period = 11;
		CourseIntervalSet<String> cis = new CourseIntervalSet<>(period, mis);
		try {
			cis.insert(5, 9, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(1, cis.intervals(s1).start(0));
		assertEquals(5, cis.intervals(s1).start(1));
		assertEquals(2, cis.intervals(s1).end(0));
		assertEquals(9, cis.intervals(s1).end(1));
		
	}
}
