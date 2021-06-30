package Specific;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import decorator.PeriodicMultiIntervalSet;


public class CourseIntervalSet<L> extends PeriodicMultiIntervalSet<L> implements MultiIntervalSet<L> {

	public CourseIntervalSet(long period, IntervalSet<L> initial) {
		super(period, MultiIntervalSet.init(initial));
	}
	
	public CourseIntervalSet(long period) {
		super(period, MultiIntervalSet.empty());
	}

}
