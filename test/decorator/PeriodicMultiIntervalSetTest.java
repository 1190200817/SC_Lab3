package decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class PeriodicMultiIntervalSetTest {

	// 因为PeriodicMultiIntervalSet只有insert方法不同，因此只测试insert方法
	@Test
	void insertTest() {
		MultiIntervalSet<String> mis = MultiIntervalSet.empty();
		long period = 11;
		PeriodicMultiIntervalSet<String> pis = new PeriodicMultiIntervalSet<>(period, mis);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 无周期插入成功
		try {
			pis.insert(1, 2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(1, pis.intervals(s1).labels().size());
		assertEquals(1, pis.intervals(s1).start(0));
		assertEquals(2, pis.intervals(s1).end(0));
		
		try {
			pis.insert(3, 6, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.intervals(s1).labels().size());
		assertEquals(1, pis.intervals(s1).start(0));
		assertEquals(2, pis.intervals(s1).end(0));
		assertEquals(3, pis.intervals(s1).start(1));
		assertEquals(6, pis.intervals(s1).end(1));
		
		pis.remove(s1);
		assertEquals(0, pis.labels().size());
		
		// start >= end
		try {
			pis.insert(2, 1, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// start < 0,成功
		try {
			pis.insert(-3, -2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(8, pis.intervals(s1).start(0));
		assertEquals(9, pis.intervals(s1).end(0));
		
		pis.remove(s1);
		// 跨越周期，且插入成功
		try {
			pis.insert(13, 16, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.intervals(s1).start(0));
		assertEquals(5, pis.intervals(s1).end(0));
		
		// 跨越周期，但分为两半，插入成功
		pis.remove(s1);
		assertEquals(0, pis.labels().size());
		try {
			pis.insert(10, 12, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.intervals(s1).labels().size());
		assertEquals(0, pis.intervals(s1).start(0));
		assertEquals(1, pis.intervals(s1).end(0));
		assertEquals(10, pis.intervals(s1).start(1));
		assertEquals(11, pis.intervals(s1).end(1));
		
		pis.remove(s1);
		// 重复插入相同时间段，不出错
		try {
			pis.insert(5, 9, s1);
			pis.insert(16, 20, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(5, pis.intervals(s1).start(0));
		assertEquals(9, pis.intervals(s1).end(0));
		
		// 重复插入不冲突时间段，不出错
		try {
			pis.insert(21, 22, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.intervals(s1).labels().size());
		assertEquals(5, pis.intervals(s1).start(0));
		assertEquals(9, pis.intervals(s1).end(0));
		assertEquals(10, pis.intervals(s1).start(1));
		assertEquals(11, pis.intervals(s1).end(1));
		
		pis.remove(s1);
		
		// 重复插入冲突时间段，出错
		boolean flag = false;
		try {
			pis.insert(3, 7, s1);
			pis.insert(17, 19, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, pis.labels().size());
		assertEquals(1, pis.intervals(s1).labels().size());
		assertEquals(3, pis.intervals(s1).start(0));
		assertEquals(7, pis.intervals(s1).end(0));
		pis.remove(s1);
		
		// 时间段长度大于period
		try {
			pis.insert(23, 35, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// 时间段长度等于period
		try {
			pis.insert(23, 34, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(0, pis.intervals(s1).start(0));
		assertEquals(11, pis.intervals(s1).end(0));
		pis.remove(s1);
		// 跨越周期的多标签插入
		try {
			pis.insert(22, 24, s1);
			pis.insert(39, 42, s2);
			pis.insert(21, 24, s2);
			pis.insert(44, 55, s3);  // 跨越整个周期
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, pis.labels().size());
		assertEquals(0, pis.intervals(s1).start(0));
		assertEquals(2, pis.intervals(s1).end(0));
		assertEquals(0, pis.intervals(s2).start(0));
		assertEquals(2, pis.intervals(s2).end(0));
		assertEquals(6, pis.intervals(s2).start(1));
		assertEquals(9, pis.intervals(s2).end(1));
		assertEquals(10, pis.intervals(s2).start(2));
		assertEquals(11, pis.intervals(s2).end(2));
		assertEquals(0, pis.intervals(s3).start(0));
		assertEquals(11, pis.intervals(s3).end(0));	 
}

}
