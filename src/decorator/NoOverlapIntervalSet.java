package decorator;


import java.util.HashMap;
import java.util.Map;

import Assist.time;
import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * ��ͬ��ǩ��ʱ��β������ص���IntervalSet
 * 
 * @author ��С��
 * @param <L>
 */
public class NoOverlapIntervalSet<L> extends IntervalSetDecorator<L> implements IntervalSet<L>{

	private final Map<L, time> intervalMap = new HashMap<>(); 
	
	// Abstraction function:
    //  intervalMap��¼һ��ʱ��ε�ӳ�䣬ÿ������ʾһ����ǩ����Ӧ��ֵ����Ӧ��ʱ���
    // Representation invariant:
    //  ÿһ��ʱ��εĽ���ʱ����ڿ�ʼʱ��
	//  ÿ��ʱ��εĿ�ʼʱ���ǷǸ���
	//  ���е�ʱ��β��ص�
    // Safety from rep exposure:
    //  ����ʹ����private��final���������Σ���û�з������Եķ���
	
	public NoOverlapIntervalSet(IntervalSet<L> intervalSet) {
		super(intervalSet);
		checkRep();
	}
	
	private void checkRep() {
		for(L label:intervalMap.keySet()) {
			time t = intervalMap.get(label);
			assert t.getStart() >= 0;
			assert t.getStart() < t.getEnd();
			for(L label2:intervalMap.keySet()) {
				assert label.equals(label2) || intervalMap.get(label).getStart() >= intervalMap.get(label2).getEnd() ||
				intervalMap.get(label).getEnd() <= intervalMap.get(label2).getStart();
			}
		}
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException {
		if(start >= end)
			return;
		super.insert(start, end, label);
		for(L label2:intervalMap.keySet()) {
			if(label2.equals(label))
				continue;
			time t = intervalMap.get(label2);
			if(start >= t.getEnd() | end <= t.getStart())
				continue;
			else {
				super.remove(label);
				throw new IntervalConflictException("insert error: interval conflict between different label");
			}
		}
		intervalMap.put(label, new time(start, end));
		checkRep();
	}
	
	@Override
	public boolean remove(L label) {
		intervalMap.remove(label);
		return super.remove(label);
	}
	
	@Override
	public IntervalSet<L> copy() {
		return new NoOverlapIntervalSet<>(this.intervalSet.copy());
	}
}
