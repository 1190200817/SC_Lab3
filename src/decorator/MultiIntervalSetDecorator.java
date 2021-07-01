package decorator;

import java.util.Set;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * MultiIntervalSetװ����
 * @author ��С��
 *
 * @param <L>
 */
public abstract class MultiIntervalSetDecorator<L> implements MultiIntervalSet<L>{

	protected final MultiIntervalSet<L> multiIntervalSet;
	
	 // Abstraction function:
    //  multiIntervalSet����Ҫ���ε�MultiIntervalSet��
    // Representation invariant:
    //  multiIntervalSet��������MultiIntervalSet
    // Safety from rep exposure:
    //  ʹ��final����multiIntervalSet
	//	û�з�����ֱ�ӷ���multiIntervalSet������������ʽ����
	
	public MultiIntervalSetDecorator(MultiIntervalSet<L> multiIntervalSet) {
		this.multiIntervalSet = multiIntervalSet;
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException {
		this.multiIntervalSet.insert(start, end, label);
	}
	
	@Override
	public boolean remove(L label) {
		return this.multiIntervalSet.remove(label);
	}
	
	@Override
	public Set<L> labels() {
		return this.multiIntervalSet.labels();
	}
	
	@Override
	public IntervalSet<Integer> intervals(L label) {
		return this.multiIntervalSet.intervals(label);
	}
}
