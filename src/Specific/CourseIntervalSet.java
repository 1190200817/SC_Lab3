package Specific;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import decorator.PeriodicMultiIntervalSet;

/**
 * 针对课表管理的IntervalSet
 * @author 刘小川
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
