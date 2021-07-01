package Specific;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import decorator.NoOverlapIntervalSet;
import decorator.NoOverlapMultiIntervalSet;

/**
 * 针对操作系统进程调度管理的IntervalSet
 * @author 刘小川
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
