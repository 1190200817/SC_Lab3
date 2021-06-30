package MultiIntervalSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Assist.time;
import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
	
	private final ArrayList<IntervalSet<L>> intervalList = new ArrayList<>();

	// Abstraction function:
    //  intervalList��IntervalSet���б�IntervalSet��ÿ������ʾһ����ǩ����Ӧ��ֵ����Ӧ��ʱ���
	// һ����ǩ���Զ�Ӧ���ʱ��Σ��ֲ��ڲ�ͬ��IntervalSet��
    // Representation invariant:
    //  ÿһ��ʱ��εĽ���ʱ����ڿ�ʼʱ��
	//  ÿһ��ʱ��εĿ�ʼʱ��Ǹ�
	//  ÿһ����ǩ��Ӧ��ʱ��β����ص�
	//  ����һ����ǩ��˵�������е�ʱ��μ�����intervalList��ǰ����������
	//  intervalList������һ��Ԫ��
    // Safety from rep exposure:
    //  ����ʹ����private��final���������Σ���û�з������Եķ���
	
	/**
	 * Constructor
	 */
	public CommonMultiIntervalSet() {
		this(IntervalSet.empty());
		checkRep();
	}
	
	/**
	 * Constructor: construct a MultiIntervalSet from a IntervalSet
	 * @param initial a source IntervalSet
	 */
	public CommonMultiIntervalSet(IntervalSet<L> initial) {
		intervalList.add(initial.copy());
		checkRep();
	}
	
	private void checkRep() {
		assert intervalList.size() > 0;
		
		Set<L> labelSet = intervalList.get(0).labels();
		for(L label:labelSet) {
			Set<time> field = new HashSet<time>();
			int brk = intervalList.size();
			for(int i = 0; i < intervalList.size(); i++) {
				long start = intervalList.get(i).start(label);
				long end = intervalList.get(i).end(label);
				if(start < 0) {
					brk = i;
					break;
				}
				assert start >= 0;
				assert start < end;
				for(time t:field) {
					assert t.getStart() >= end || t.getEnd() <= start;
				}
				field.add(new time(start, end));
			}
			for(int i = brk; i < intervalList.size(); i++) {
				assert ! (intervalList.get(i).labels().contains(label));
			}
		}	
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException{
		if(start < 0)
			throw new StartNegativeException();
		if(start >= end){
			return;
		}
		
		for(int i = 0; i < intervalList.size(); i++) {
			IntervalSet<L> interval = intervalList.get(i);
			if(interval.labels().contains(label)) {
				long s = interval.start(label);
				long e = interval.end(label);
				if(start == s && end == e)
					return;
				if(s >= end || e <= start)
					continue;
				else {
					throw new IntervalConflictException("insert error:same label, overlapping intervals");
				}
			}
			else {
				interval.insert(start, end, label);
				checkRep();
				return;
			}
		}
		
		IntervalSet<L> interval = IntervalSet.empty();
		interval.insert(start, end, label);	
		intervalList.add(interval);
		checkRep();
	}
	
	@Override
	public Set<L> labels(){	
		return intervalList.get(0).labels();
	}
	
	@Override
	public boolean remove(L label) {
		boolean res = false;
		for(IntervalSet<L> interval:intervalList) {
			if(interval.remove(label))
				res = true;
		}
		Iterator<IntervalSet<L>> iter = intervalList.iterator();
		while(iter.hasNext()) {
			if(iter.next().labels().size() == 0)
				iter.remove();
		}
		if(intervalList.size() == 0) {
			intervalList.add(IntervalSet.empty());
		}
		checkRep();
		return res;
	}
	
	@Override
	public IntervalSet<Integer> intervals(L label){
		IntervalSet<Integer> res = IntervalSet.empty();
		List<time> timeList = new ArrayList<>();
		for(IntervalSet<L> interval:intervalList) {
			long start = interval.start(label);
			long end = interval.end(label);
			if(start >= 0) {
				timeList.add(new time(start, end));
			}
		}
		Collections.sort(timeList);
		int intLabel = 0;
		for(time t:timeList) {
			try {
				res.insert(t.getStart(), t.getEnd(), intLabel);
			} catch (IntervalConflictException | StartNegativeException e) {
				e.printStackTrace();
			}
			intLabel += 1;
		}
		return res;		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(L label:labels()) {
			sb.append(label.toString());
			sb.append(",time:");
			IntervalSet<Integer> insInt = intervals(label);
			int loc = 0;
			while(insInt.labels().contains(loc)) {
				sb.append("[" + insInt.start(loc) + "," + insInt.end(loc) + ")");
				loc += 1;
			}
			sb.append(";");
		}
		
 		return sb.toString();
	}
	
}
