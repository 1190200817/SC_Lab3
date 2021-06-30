package decorator;

import java.util.HashSet;
import java.util.Set;

import Assist.time;
import IntervalSet.IntervalSet;

/**
 * NoBlankIntervalSet<L>��һ��ʵ����
 * 
 * @author ��С��
 *
 * @param <L>
 */
public class CommonNoBlankIntervalSet<L> extends IntervalSetDecorator<L> implements NoBlankIntervalSet<L> {
	
	private final long startTime;
	private final long endTime;
	
	// Abstraction function:
    //  startTime���ܵĿ�ʼʱ�䣬endTime���ܵĽ���ʱ��
    // Representation invariant:
    //  endTime > startTime
	//  startTime >= 0
    // Safety from rep exposure:
    //  startTime,endTimeʹ����private��final�������Σ�����long�ǲ��ɱ�����
	
	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	public CommonNoBlankIntervalSet(long startTime, long endTime, IntervalSet<L> intervalSet) {
		super(intervalSet);
		this.startTime = startTime;
		this.endTime = endTime;
		checkRep();
	}
	
	private void checkRep() {
		assert endTime > startTime;
		assert startTime >= 0;
	}
	
	@Override
	public Set<time> blankIntervals() {
		Set<time> res = new HashSet<time>();
		res.add(new time(startTime, endTime));
		for(L label: this.labels()) {
			time t1 = new time(this.start(label), this.end(label));
			Set<time> tempSet = new HashSet<time>(res);
			for(time t2: tempSet) {
				if(t1.getStart() >= t2.getEnd() || t1.getEnd() <= t2.getStart())
					continue;
				else {
					res.remove(t2);
					if(t1.getStart() > t2.getStart()) {
						res.add(new time(t2.getStart(), t1.getStart()));
					}
					if(t1.getEnd() < t2.getEnd()) {
						res.add(new time(t1.getEnd(), t2.getEnd()));
					}
				}
			}
		}
		checkRep();
		return res;
	}
	
	@Override
	public IntervalSet<L> copy() {
		return new CommonNoBlankIntervalSet<>(this.startTime, this.endTime, this.intervalSet.copy());
	}
	
}
