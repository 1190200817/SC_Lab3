package Specific;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import IntervalSet.IntervalSet;
import decorator.NoOverlapIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

class ProcessIntervalSetTest {

	// ��Ҫ����ProcessIntervalSet�Ƿ��������ά���ϵ�����
	
	// NoOverlap����
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
	
	// �������� �� ����հ� �������

	// ��ʼ������
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
