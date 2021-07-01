package decorator;

import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * 具有周期性时间的MultiIntervalSet
 * 周期period一定是大于零的整数
 *
 * @author 刘小川
 *
 * @param <L>
 */
public class PeriodicMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> implements MultiIntervalSet<L>{

	private final long period;
	
	// Abstraction function:
    //  period表示时间周期
    // Representation invariant:
    //  period > 0
    // Safety from rep exposure:
    //  period使用了private和final进行修饰，并且long是不可变类型
	
	public PeriodicMultiIntervalSet(long period, MultiIntervalSet<L> multiIntervalSet) {
		super(multiIntervalSet);
		this.period = period;
		checkRep();
	}
	
	private void checkRep() {
		assert period > 0;
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException {
		if(start >= end || (end - start) > this.period)
			return;
		long startM = start % this.period;
		long endM = end % this.period;
		if(startM < 0)
			startM += this.period;
		if(endM <= 0)
			endM += this.period;
		
		if(endM < startM) {
			super.insert(0, endM, label);
			super.insert(startM, this.period, label);
		}
		else if(endM == startM)
			super.insert(0, this.period, label);
		else {
			super.insert(startM, endM, label);
		}
		checkRep();
	}
}
