package MultiIntervalSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * Tests for instance methods of MultiIntervalSet.
 */

public class MultiIntervalSetTest {
	
	/*
	 * Testing MultiIntervalSet...
	 */

	// Testing strategy for MultiIntervalSetTest.toString()
	// 添加一些时间段，测试toString是否正确输出

	// TODO tests for MultiIntervalSetTest.toString()
	@Test
	public void toStringTest() {
		MultiIntervalSet<String> mul = MultiIntervalSet.empty();
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		try {
			mul.insert(3, 5, s1);
			mul.insert(5, 9, s1);
			mul.insert(0, 7, s2);
			mul.insert(3, 4, s3);
			mul.insert(6, 10, s3);
			mul.insert(4, 5, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		assertEquals("bcd,time:[0,7);abc,time:[3,5)[5,9);def,time:[3,4)[4,5)[6,10);", mul.toString());

		mul.remove("bcd");
		assertEquals("abc,time:[3,5)[5,9);def,time:[3,4)[4,5)[6,10);", mul.toString());
		
	}

	@Test
	public void insertTest() {
		MultiIntervalSet<String> mul = MultiIntervalSet.empty();
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 正常插入
		try {
			mul.insert(0, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		mul.remove(s1);
		
		// start < 0
		boolean flag = false;
		try {
			mul.insert(-1, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		finally {
			assertTrue(flag);
			assertEquals(0, mul.labels().size());
			assertEquals(false, mul.labels().contains(s1));
		}
		
		// start >= end
		try {
			mul.insert(4, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		finally {
			assertEquals(0, mul.labels().size());
			assertEquals(false, mul.labels().contains(s1));
		}

		try {
			mul.insert(4, 4, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		finally {
			assertEquals(0, mul.labels().size());
			assertEquals(false, mul.labels().contains(s1));
		}
		
		// 标签重复，但时间段一致
		try {
			mul.insert(0, 3, s1);
			mul.insert(0, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(1, mul.intervals(s1).labels().size());

		// 标签重复，时间段不冲突
		try {
			mul.insert(4, 6, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(2, mul.intervals(s1).labels().size());
		
		// 标签重复，时间段冲突
		flag = false;
		try {
			mul.remove(s1);
			mul.insert(4, 7, s1);
			mul.insert(2, 3, s1);
			mul.insert(3, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(true, flag);
		
		// 标签不重复
		try {
			mul.insert(4, 7, s2);
			mul.insert(2, 3, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(2, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(2, mul.intervals(s2).labels().size());
		try {
			mul.insert(4, 7, s3);
			mul.insert(2, 3, s3);
			mul.insert(9, 10, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(2, mul.intervals(s2).labels().size());
		assertEquals(3, mul.intervals(s3).labels().size());
		
		// remove作用
		mul.remove(s2);
		assertEquals(2, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(false, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(3, mul.intervals(s3).labels().size());
		try {
			mul.insert(4, 7, s2);
			mul.insert(2, 3, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(2, mul.intervals(s2).labels().size());
		assertEquals(3, mul.intervals(s3).labels().size());
		
	}
	
	
	@Test
	public void initTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		try {
			inv.insert(2, 5, s1);
			inv.insert(3, 7, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		MultiIntervalSet<String> mul = MultiIntervalSet.init(inv);
		// 从IntervalSet初始化
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(false, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		
		assertEquals(1, mul.intervals(s1).labels().size());
		assertEquals(1, mul.intervals(s3).labels().size());
		// 初始化后的操作发生overlap
		boolean flag = false;
		try {
			mul.insert(4, 7, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, mul.intervals(s1).labels().size());
		
		// 正常的插入、删除
		try {
			mul.insert(6, 7, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(2, mul.intervals(s1).labels().size());
		
		try {
			mul.insert(6, 7, s2);
			mul.insert(9, 10, s2);
			mul.insert(2, 5, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, mul.labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(3, mul.intervals(s2).labels().size());
		assertEquals(1, mul.intervals(s3).labels().size());
		
		mul.remove(s1);
		assertEquals(2, mul.labels().size());
		assertEquals(false, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(3, mul.intervals(s2).labels().size());
		assertEquals(1, mul.intervals(s3).labels().size());
		
		mul.remove(s2);
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(1, mul.intervals(s3).labels().size());
		
		mul.remove(s3);
		assertEquals(0, mul.labels().size());
	
	}
	
	@Test
	public void removeTest() {
		MultiIntervalSet<String> mul = MultiIntervalSet.empty();
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 删除失败
		assertFalse(mul.remove(s1));
		
		// 删除成功
		try {
			mul.insert(4, 7, s1);
			mul.insert(2, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(2, mul.intervals(s1).labels().size());
		assertTrue(mul.remove(s1));
		assertEquals(false, mul.labels().contains(s1));
		assertEquals(0, mul.intervals(s1).labels().size());
		
		// 多个标签删除
		try {
			mul.insert(4, 7, s1);
			mul.insert(2, 3, s1);
			mul.insert(5, 9, s2);
			mul.insert(2, 4, s2);
			mul.insert(0, 1, s3);
			mul.insert(6, 8, s3);
			mul.insert(9, 11, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, mul.labels().size());
		assertEquals(2, mul.intervals(s1).labels().size());
		assertEquals(true, mul.labels().contains(s1));
		assertEquals(true, mul.labels().contains(s2));
		assertEquals(true, mul.labels().contains(s3));
		assertTrue(mul.remove(s1));
		assertEquals(2, mul.labels().size());
		assertEquals(false, mul.labels().contains(s1));
		assertEquals(2, mul.intervals(s2).labels().size());
		assertEquals(3, mul.intervals(s3).labels().size());
		mul.remove(s2);
		assertEquals(1, mul.labels().size());
		assertEquals(true, mul.labels().contains(s3));
		assertEquals(3, mul.intervals(s3).labels().size());
		mul.remove(s3);
		assertEquals(0, mul.labels().size());
	}
	
	@Test
	public void labelsTest() {
		MultiIntervalSet<String> mul = MultiIntervalSet.empty();
		Set<String> res = new HashSet<>();
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// 添加成功
		try {
			mul.insert(4, 7, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		res.add(s1);
		assertEquals(res, mul.labels());
		
		// 添加失败
		boolean flag = false;
		try {
			mul.insert(-2, 4, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(res, mul.labels());
		try {
			mul.insert(4, 4, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(res, mul.labels());
		
		// 删除失败
		mul.remove(s2);
		assertEquals(res, mul.labels());
		
		// 删除成功
		mul.remove(s1);
		res.remove(s1);
		assertEquals(res, mul.labels());
		
		// 多个标签
		try {
			mul.insert(4, 7, s1);
			mul.insert(2, 3, s1);
			mul.insert(5, 9, s2);
			mul.insert(2, 4, s2);
			mul.insert(0, 1, s3);
			mul.insert(6, 8, s3);
			mul.insert(9, 11, s3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		res.add(s1);
		res.add(s2);
		res.add(s3);
		assertEquals(res, mul.labels());
	}
	
	@Test
	public void intervalsTest() {
		MultiIntervalSet<String> mul = MultiIntervalSet.empty();
		IntervalSet<Integer> inv;
		String s1 = "abc";
		// 返回空
		inv = mul.intervals(s1);
		assertEquals(0, inv.labels().size());
		
		// 一个时间段
		try {
			mul.insert(0, 3, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		inv = mul.intervals(s1);
		assertEquals(1, inv.labels().size());
		assertEquals(0, inv.start(0));
		assertEquals(3, inv.end(0));
		
		// 多个时间段
		try {
			mul.insert(6, 8, s1);
			mul.insert(4, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		inv = mul.intervals(s1);
		assertEquals(3, inv.labels().size());
		assertEquals(0, inv.start(0));
		assertEquals(3, inv.end(0));
		assertEquals(4, inv.start(1));
		assertEquals(5, inv.end(1));
		assertEquals(6, inv.start(2));
		assertEquals(8, inv.end(2));
		
		// remove
		mul.remove(s1);
		inv = mul.intervals(s1);
		assertEquals(0, inv.labels().size());
		
	}

}
