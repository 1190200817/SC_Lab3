package APIs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class APIsTest {

	// 测试Similarity函数
	@Test
	public void similarityTest() {
		MultiIntervalSet<String> mis1 = MultiIntervalSet.empty();
		MultiIntervalSet<String> mis2 = MultiIntervalSet.empty();
		String a = "A";
		String b = "B";
		String c = "C";
		
		// 存在空set
		assertEquals(1.0, APIs.Similarity(mis1,mis2));
		// 指导书示例
		try {
			mis1.insert(0, 5, a);
			mis1.insert(20, 25, a);
			mis1.insert(10, 20, b);
			mis1.insert(25, 30, b);
			mis2.insert(20, 35, a);
			mis2.insert(10, 20, b);
			mis2.insert(0, 5, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(15.0/35, APIs.Similarity(mis1, mis2));
		
		// 存在多次交叉
		mis2.remove(a);
		try {
			mis2.insert(20, 26, a);
			mis2.insert(27, 28, b);
			mis2.insert(29, 35, b);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(17.0/35, APIs.Similarity(mis1, mis2));
		
		mis2.remove(a);
		mis2.remove(b);
		mis2.remove(c);
		mis1.remove(a);
		mis1.remove(b);
		mis1.remove(c);
		try {
			mis1.insert(5, 10, a);
			mis1.insert(12, 20, a);
			mis1.insert(25, 30, a);
			mis2.insert(0, 6, a);
			mis2.insert(7, 8, a);
			mis2.insert(9, 14, a);
			mis2.insert(17, 18, a);
			mis2.insert(19, 25, a);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(7.0/30, APIs.Similarity(mis1, mis2));
		
		try {
			mis1.insert(21, 22, a);
			mis1.insert(23, 24, a);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(9.0/30, APIs.Similarity(mis1, mis2));
	}
	
	// 测试calcConflictRatio函数
	@Test
	public void calcConflictRatioForMultiTest() {
		MultiIntervalSet<String> mis1 = MultiIntervalSet.empty();
		String a = "A";
		String b = "B";
		String c = "C";
		
		// 空set
		assertEquals(0.0, APIs.calcConflictRatio(mis1));
		
		// 两个标签冲突
		try {
			mis1.insert(0, 5, a);
			mis1.insert(10, 20, b);
			mis1.insert(25, 30, b);
			mis1.insert(20, 35, a);
			mis1.insert(0, 5, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(10.0/35, APIs.calcConflictRatio(mis1));
		
		// 多个标签冲突
		mis1.remove(a);
		mis1.remove(b);
		mis1.remove(c);
		try {
			mis1.insert(0, 5, a);
			mis1.insert(2, 4, b);
			mis1.insert(3, 10, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3.0/10, APIs.calcConflictRatio(mis1));
		
		try {
			mis1.insert(9, 15, a);
			mis1.insert(13, 20, b);
			mis1.insert(11, 17, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(10.0/20, APIs.calcConflictRatio(mis1));
	}
	
	// 测试calcConflictRatio函数
	@Test
	public void calcConflictRatioForSingleTest() {
		IntervalSet<String> ins = IntervalSet.empty();
		String a = "A";
		String b = "B";
		String c = "C";
		String d = "D";
		
		// 空set
		assertEquals(0.0, APIs.calcConflictRatio(ins));
				
		// 两个标签冲突
		try {
			ins.insert(0, 5, a);
			ins.insert(10, 20, b);
			ins.insert(0, 6, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(5.0/20, APIs.calcConflictRatio(ins));
		
		// 多个标签冲突
		ins.remove(a);
		ins.remove(b);
		ins.remove(c);
		try {
			ins.insert(0, 10, a);
			ins.insert(8, 15, b);
			ins.insert(6, 13, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(7.0/15, APIs.calcConflictRatio(ins));
		
		try {
			ins.insert(7, 12, d);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(7.0/15, APIs.calcConflictRatio(ins));

	}
	
	// 测试calcFreeTimeRatio函数
	@Test
	public void calcFreeTimeRatioForMultiTest() {
		MultiIntervalSet<String> mis1 = MultiIntervalSet.empty();
		String a = "A";
		String b = "B";
		String c = "C";
		
		// 空set
		assertEquals(1.0, APIs.calcFreeTimeRatio(mis1));
				
		// 无Overlap时的空闲比例
		try {
			mis1.insert(1, 5, a);
			mis1.insert(25, 30, c);
			mis1.insert(10, 20, b);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(10.0/29, APIs.calcFreeTimeRatio(mis1));
		mis1.remove(a);
		mis1.remove(b);
		mis1.remove(c);
		
		// 存在Overlap时的空闲比例
		try {
			mis1.insert(1, 6, a);
			mis1.insert(10, 20, b);
			mis1.insert(7, 13, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1.0/19, APIs.calcFreeTimeRatio(mis1));
		mis1.remove(a);
		mis1.remove(b);
		mis1.remove(c);
		
		try {
			mis1.insert(1, 6, a);
			mis1.insert(10, 20, a);
			mis1.insert(25, 30, b);
			mis1.insert(9, 25, b);
			mis1.insert(7, 18, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1.0/29, APIs.calcFreeTimeRatio(mis1));
		mis1.remove(a);
		mis1.remove(b);
		mis1.remove(c);
		
		try {
			mis1.insert(1, 6, a);
			mis1.insert(10, 20, a);
			mis1.insert(25, 30, b);
			mis1.insert(5, 22, b);
			mis1.insert(16, 24, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1.0/29, APIs.calcFreeTimeRatio(mis1));
	}
	
	// 测试calcFreeTimeRatio函数
	@Test
	public void calcFreeTimeRatioForSingleTest() {
		IntervalSet<String> ins = IntervalSet.empty();
		String a = "A";
		String b = "B";
		String c = "C";
		String d = "D";
		
		// 空set
		assertEquals(1.0, APIs.calcFreeTimeRatio(ins));
				
		// 无Overlap时的空闲比例
		try {
			ins.insert(1, 5, a);
			ins.insert(25, 30, c);
			ins.insert(10, 20, b);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(10.0/29, APIs.calcFreeTimeRatio(ins));
		ins.remove(a);
		ins.remove(b);
		ins.remove(c);
		
		// 存在Overlap时的空闲比例
		try {
			ins.insert(1, 6, a);
			ins.insert(10, 20, b);
			ins.insert(7, 13, c);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1.0/19, APIs.calcFreeTimeRatio(ins));
		ins.remove(a);
		ins.remove(b);
		ins.remove(c);
		
		try {
			ins.insert(1, 6, a);
			ins.insert(10, 20, b);
			ins.insert(25, 30, c);
			ins.insert(9, 25, d);
			ins.insert(7, 18, "e");
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1.0/29, APIs.calcFreeTimeRatio(ins));
	}

}
