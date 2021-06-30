package decorator;

import java.util.HashSet;
import java.util.Set;

import Assist.time;
import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * 不同标签的时间段不允许重叠的MultiIntervalSet
 * 
 * @author 刘小川
 * @param <L>
 */
public class NoOverlapMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> implements MultiIntervalSet<L>{

	public NoOverlapMultiIntervalSet(MultiIntervalSet<L> multiIntervalSet) {
		super(multiIntervalSet);
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException {
		Set<time> timeSet = new HashSet<>();
		for(L label2:super.multiIntervalSet.labels()) {
			for(Integer in:super.multiIntervalSet.intervals(label2).labels()) {
				long s = super.multiIntervalSet.intervals(label2).start(in);
				long e = super.multiIntervalSet.intervals(label2).end(in);
				timeSet.add(new time(s, e));
			}
		}
		for(time t:timeSet) {
			if(t.getStart() >= end || t.getEnd() <= start)
				continue;
			else {
				throw new IntervalConflictException("insert error:intervals overlap");
			}
		}
		super.insert(start, end, label);
	}
	
}
