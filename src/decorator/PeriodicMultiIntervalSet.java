package decorator;

import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * ����������ʱ���MultiIntervalSet
 * ����periodһ���Ǵ����������
 *
 * @author ��С��
 *
 * @param <L>
 */
public class PeriodicMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> implements MultiIntervalSet<L>{

	private final long period;
	
	// Abstraction function:
    //  period��ʾʱ������
    // Representation invariant:
    //  period > 0
    // Safety from rep exposure:
    //  periodʹ����private��final�������Σ�����long�ǲ��ɱ�����
	
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
