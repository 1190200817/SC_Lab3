package decorator;


import java.util.HashMap;
import java.util.Map;

import Assist.time;
import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * 不同标签的时间段不允许重叠的IntervalSet
 * 
 * @author 刘小川
 * @param <L>
 */
public class NoOverlapIntervalSet<L> extends IntervalSetDecorator<L> implements IntervalSet<L>{

	private final Map<L, time> intervalMap = new HashMap<>(); 
	
	// Abstraction function:
    //  intervalMap记录一个时间段的映射，每个键表示一个标签，对应的值是相应的时间段
    // Representation invariant:
    //  每一个时间段的结束时间大于开始时间
	//  每个时间段的开始时间是非负数
	//  所有的时间段不重叠
    // Safety from rep exposure:
    //  属性使用了private和final进行了修饰，且没有返回属性的方法
	
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
