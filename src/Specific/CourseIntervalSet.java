package Specific;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import decorator.PeriodicMultiIntervalSet;

/**
 * ��Կα�����IntervalSet
 * @author ��С��
 *
 * @param <L>
 */
public class CourseIntervalSet<L> extends PeriodicMultiIntervalSet<L> implements MultiIntervalSet<L> {

	public CourseIntervalSet(long period, IntervalSet<L> initial) {
		super(period, MultiIntervalSet.init(initial));
	}
	
	public CourseIntervalSet(long period) {
		super(period, MultiIntervalSet.empty());
	}

}
