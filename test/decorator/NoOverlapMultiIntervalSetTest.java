package decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class NoOverlapMultiIntervalSetTest {

	// ��ΪNoOverlapMultiIntervalSetֻ��insert������ͬ�����ֻ����insert����
	@Test
	void insertTest() {
		MultiIntervalSet<String> mis = MultiIntervalSet.empty();
		NoOverlapMultiIntervalSet<String> nlm = new NoOverlapMultiIntervalSet<>(mis);
		String s1 = "abc";
		String s2 = "bcd";
		
		// ��������
		try {
			nlm.insert(3, 5, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, nlm.labels().size());
		assertEquals(true, nlm.labels().contains(s1));
		assertEquals(1, nlm.intervals(s1).labels().size());
		assertEquals(3, nlm.intervals(s1).start(0));
		assertEquals(5, nlm.intervals(s1).end(0));
		
		try {
			nlm.insert(6, 10, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
		assertEquals(1, nlm.labels().size());
		assertEquals(true, nlm.labels().contains(s1));
		assertEquals(2, nlm.intervals(s1).labels().size());
		assertEquals(3, nlm.intervals(s1).start(0));
		assertEquals(5, nlm.intervals(s1).end(0));
		assertEquals(6, nlm.intervals(s1).start(1));
		assertEquals(10, nlm.intervals(s1).end(1));
		
		// ����ǩOverlap
		boolean flag = false;
		try {
			nlm.insert(4, 6, s1);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, nlm.labels().size());
		assertEquals(true, nlm.labels().contains(s1));
		assertEquals(2, nlm.intervals(s1).labels().size());
		assertEquals(3, nlm.intervals(s1).start(0));
		assertEquals(5, nlm.intervals(s1).end(0));
		assertEquals(6, nlm.intervals(s1).start(1));
		assertEquals(10, nlm.intervals(s1).end(1));
		
		// ���ǩOverlap
		flag = false;
		try {
			nlm.insert(4, 7, s2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
		assertTrue(flag);
		assertEquals(1, nlm.labels().size());
		assertEquals(false, nlm.labels().contains(s2));
		
	}

}
