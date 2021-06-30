package decorator;


import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * ����������ʱ���IntervalSet
 * ����periodһ���Ǵ����������
 * @author ��С��
 *
 * @param <L>
 */
public class PeriodicIntervalSet<L> extends IntervalSetDecorator<L> implements IntervalSet<L> {

	private final long period;
	
	// Abstraction function:
    //  period��ʾʱ������
    // Representation invariant:
    //  period > 0
	//  ���е�ʱ��β��ܳ���period
    // Safety from rep exposure:
    //  periodʹ����private��final�������Σ�����long�ǲ��ɱ�����
	
	public PeriodicIntervalSet(long period,IntervalSet<L> intervalSet) {
		super(intervalSet);
		this.period = period;
		checkRep();
	}
	
	private void checkRep() {
		assert period > 0;
		for(L label:this.labels()) {
			assert this.start(label) < period;
			assert this.end(label) <= period;
 		}
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
		if(startM == endM) {
			super.insert(0, this.period, label);
		}
		super.insert(startM, endM, label);
		checkRep();
	}
	
	@Override
	public IntervalSet<L> copy() {
		return new PeriodicIntervalSet<>(period, this.intervalSet.copy());
	}
}
