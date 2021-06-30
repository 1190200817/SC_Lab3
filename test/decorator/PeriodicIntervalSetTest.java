package decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class PeriodicIntervalSetTest {

	// ��ΪPeriodicIntervalSetֻ��insert������ͬ�����ֻ����insert����
	@Test
	void insertTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		long period = 11;
		PeriodicIntervalSet<String> pis = new PeriodicIntervalSet<String>(period, inv);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// �����ڲ���ɹ�
		try {
			pis.insert(1, 2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(1, pis.start(s1));
		assertEquals(2, pis.end(s1));
		pis.remove(s1);
		// start >= end
		try {
			pis.insert(2, 1, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// start < 0 ʧ��
		boolean flag = false;
		try {
			pis.insert(-3, 1, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// start < 0 �ɹ�
		try {
			pis.insert(-3, -2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(8, pis.start(s1));
		assertEquals(9, pis.end(s1));
		
		pis.remove(s1);
		// ��Խ���ڣ��Ҳ���ɹ�
		try {
			pis.insert(13, 16, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.start(s1));
		assertEquals(5, pis.end(s1));
		
		// ��Խ���ڣ�����Ϊ���룬����ʧ��
		pis.remove(s1);
		assertEquals(0, pis.labels().size());
		try {
			pis.insert(10, 12, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// �ظ�������ͬʱ��Σ�������
		try {
			pis.insert(5, 9, s1);
			pis.insert(16, 20, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(5, pis.start(s1));
		assertEquals(9, pis.end(s1));
		
		// �ظ����벻ͬʱ��Σ�����
		flag = false;
		try {
			pis.insert(6, 8, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, pis.labels().size());
		assertEquals(5, pis.start(s1));
		assertEquals(9, pis.end(s1));
		pis.remove(s1);
		// ��Խ���ڵĶ��ǩ����
		try {
			pis.insert(22, 24, s1);
			pis.insert(39, 42, s2);
			pis.insert(44, 55, s3);  // ��Խ��������
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(3, pis.labels().size());
		assertEquals(0, pis.start(s1));
		assertEquals(2, pis.end(s1));
		assertEquals(6, pis.start(s2));
		assertEquals(9, pis.end(s2));
		assertEquals(0, pis.start(s3));
		assertEquals(11, pis.end(s3));	
		
		pis.remove(s1);
		pis.remove(s2);
		pis.remove(s3);
		// �������
		try {
			pis.insert(23, 34, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(0, pis.start(s1));
		assertEquals(11, pis.end(s1));
		pis.remove(s1);
		try {
			pis.insert(23, 35, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
	}
	
	@Test
	public void copyTest() {
		IntervalSet<String> inv = IntervalSet.empty();
		long period = 11;
		PeriodicIntervalSet<String> pis = new PeriodicIntervalSet<String>(period, inv);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		try {
			pis.insert(22, 24, s1);
			pis.insert(39, 42, s2);
			pis.insert(44, 55, s3);  // ��Խ��������
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		
		IntervalSet<String> pisCopy = pis.copy();
		assertEquals(pis.labels().size(), pisCopy.labels().size());
		assertEquals(pis.start(s1),pisCopy.start(s1));
		assertEquals(pis.end(s1), pisCopy.end(s1));
		assertEquals(pis.start(s2), pisCopy.start(s2));
		assertEquals(pis.end(s2), pisCopy.end(s2));
		assertEquals(pis.start(s3), pisCopy.start(s3));
		assertEquals(pis.end(s3), pisCopy.end(s3));	
	
	}

}
