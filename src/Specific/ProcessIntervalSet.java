package Specific;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import decorator.NoOverlapIntervalSet;
import decorator.NoOverlapMultiIntervalSet;

/**
 * ��Բ���ϵͳ���̵��ȹ����IntervalSet
 * @author ��С��
 *
 * @param <L>
 */
public class ProcessIntervalSet<L> extends NoOverlapMultiIntervalSet<L> implements MultiIntervalSet<L>{

	public ProcessIntervalSet() {
		this(IntervalSet.empty());
	}
	
	public ProcessIntervalSet(IntervalSet<L> initial) {
		super(MultiIntervalSet.init(new NoOverlapIntervalSet<>(initial)));
	}
	
}
