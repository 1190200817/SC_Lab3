package decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class PeriodicMultiIntervalSetTest {

	// ��ΪPeriodicMultiIntervalSetֻ��insert������ͬ�����ֻ����insert����
	@Test
	void insertTest() {
		MultiIntervalSet<String> mis = MultiIntervalSet.empty();
		long period = 11;
		PeriodicMultiIntervalSet<String> pis = new PeriodicMultiIntervalSet<>(period, mis);
		String s1 = "abc";
		String s2 = "bcd";
		String s3 = "def";
		
		// �����ڲ���ɹ�
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
		
		// start < 0,�ɹ�
		try {
			pis.insert(-3, -2, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(8, pis.intervals(s1).start(0));
		assertEquals(9, pis.intervals(s1).end(0));
		
		pis.remove(s1);
		// ��Խ���ڣ��Ҳ���ɹ�
		try {
			pis.insert(13, 16, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(2, pis.intervals(s1).start(0));
		assertEquals(5, pis.intervals(s1).end(0));
		
		// ��Խ���ڣ�����Ϊ���룬����ɹ�
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
		// �ظ�������ͬʱ��Σ�������
		try {
			pis.insert(5, 9, s1);
			pis.insert(16, 20, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(5, pis.intervals(s1).start(0));
		assertEquals(9, pis.intervals(s1).end(0));
		
		// �ظ����벻��ͻʱ��Σ�������
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
		
		// �ظ������ͻʱ��Σ�����
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
		
		// ʱ��γ��ȴ���period
		try {
			pis.insert(23, 35, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(0, pis.labels().size());
		
		// ʱ��γ��ȵ���period
		try {
			pis.insert(23, 34, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, pis.labels().size());
		assertEquals(0, pis.intervals(s1).start(0));
		assertEquals(11, pis.intervals(s1).end(0));
		pis.remove(s1);
		// ��Խ���ڵĶ��ǩ����
		try {
			pis.insert(22, 24, s1);
			pis.insert(39, 42, s2);
			pis.insert(21, 24, s2);
			pis.insert(44, 55, s3);  // ��Խ��������
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
