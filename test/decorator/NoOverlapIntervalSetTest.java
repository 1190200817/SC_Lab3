package decorator;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class NoOverlapIntervalSetTest {

	// 因为NoOverlapIntervalSet只有insert方法不同，因此只测试insert方法
	@Test
	public void insertTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		NoOverlapIntervalSet<String> noinv = new NoOverlapIntervalSet<>(inv);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 正常插入
		try {
			noinv.insert(0, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, noinv.labels().size());
		assertEquals(true, noinv.labels().contains(s1));
		noinv.remove(s1);
		
		// start < 0
		boolean flag = false;
		try {
			noinv.insert(-1, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		finally {
			assertTrue(flag);
			assertEquals(0, noinv.labels().size());
			assertEquals(false, noinv.labels().contains(s1));
		}
		
		// start >= end
		try {
			noinv.insert(4, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		finally {
			assertEquals(0, noinv.labels().size());
			assertEquals(false, noinv.labels().contains(s1));
		}

		try {
			noinv.insert(4, 4, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		finally {
			assertEquals(0, noinv.labels().size());
			assertEquals(false, noinv.labels().contains(s1));
		}
		
		// 标签重复，但时间段一致
		try {
			noinv.insert(0, 3, s1);
			noinv.insert(0, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, noinv.labels().size());
		assertEquals(true, noinv.labels().contains(s1));

		// 时间段冲突
		flag = false;
		try {
			noinv.insert(2, 6, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, noinv.labels().size());
		assertEquals(true, noinv.labels().contains(s1));
		assertEquals(false, noinv.labels().contains(s2));
		
		// 标签重复，时间段冲突
		flag = false;
		try {
			noinv.remove(s1);
			noinv.insert(4, 7, s1);
			noinv.insert(2, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(4, noinv.start(s1));
		assertEquals(7, noinv.end(s1));
		
		
		// remove作用
		noinv.remove(s1);
		try {
			noinv.insert(2, 3, s1);
			noinv.insert(4, 5, s2);
			noinv.insert(7, 9, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, noinv.labels().size());
		noinv.remove(s2);
		try {
			noinv.insert(3, 7, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, noinv.labels().size());
	}
	
	@Test
	public void copyTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		NoOverlapIntervalSet<String> noinv = new NoOverlapIntervalSet<>(inv);
		String s1 = "abc";
		String s2 = "bcd";
		try {
			noinv.insert(0, 3, s1);
			noinv.insert(4, 5, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		IntervalSet<String> noinvCopy = noinv.copy();
		assertEquals(noinv.labels(), noinvCopy.labels());
		assertEquals(noinv.start(s1), noinvCopy.start(s1));
		assertEquals(noinv.end(s1), noinvCopy.end(s1));
		assertEquals(noinv.start(s2), noinvCopy.start(s2));
		assertEquals(noinv.end(s2), noinvCopy.end(s2));
		assertEquals(noinv.remove(s1), noinvCopy.remove(s1));
		
	}

}
