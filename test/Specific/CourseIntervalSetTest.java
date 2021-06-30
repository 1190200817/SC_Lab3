package Specific;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class CourseIntervalSetTest {

	// ��Ҫ����CourseIntervalSet�Ƿ��������ά���ϵ�����
	// �����Բ���
	@Test
	public void periodicTest() {
		long period = 11;
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>(period);
		
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// �����ڲ���ɹ�
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
		
		// start < 0,�ɹ�
		try {
			cis.insert(-3, -2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(8, cis.intervals(s1).start(0));
		assertEquals(9, cis.intervals(s1).end(0));
		
		cis.remove(s1);
		// ��Խ���ڣ��Ҳ���ɹ�
		try {
			cis.insert(13, 16, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(2, cis.intervals(s1).start(0));
		assertEquals(5, cis.intervals(s1).end(0));
		
		// ��Խ���ڣ�����Ϊ���룬����ɹ�
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
		// �ظ�������ͬʱ��Σ�������
		try {
			cis.insert(5, 9, s1);
			cis.insert(16, 20, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(5, cis.intervals(s1).start(0));
		assertEquals(9, cis.intervals(s1).end(0));
		
		// �ظ����벻��ͻʱ��Σ�������
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
		
		// �ظ������ͻʱ��Σ�����
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
		
		// ʱ��γ��ȴ���period
		try {
			cis.insert(23, 35, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, cis.labels().size());
		
		// ʱ��γ��ȵ���period
		try {
			cis.insert(23, 34, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, cis.labels().size());
		assertEquals(0, cis.intervals(s1).start(0));
		assertEquals(11, cis.intervals(s1).end(0));
		cis.remove(s1);
		// ��Խ���ڵĶ��ǩ����
		try {
			cis.insert(22, 24, s1);
			cis.insert(39, 42, s2);
			cis.insert(21, 24, s2);
			cis.insert(44, 55, s3);  // ��Խ��������
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

	// Overlap����
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
	
	// ��ʼ������
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
