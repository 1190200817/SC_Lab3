package Specific;

import IntervalSet.IntervalSet;
import decorator.CommonNoBlankIntervalSet;
import decorator.NoBlankIntervalSet;
import decorator.NoOverlapIntervalSet;

public class DutyIntervalSet<L> extends CommonNoBlankIntervalSet<L> implements NoBlankIntervalSet<L> {

	public DutyIntervalSet(long startTime, long endTime) {
		super(startTime, endTime, new NoOverlapIntervalSet<L>(IntervalSet.empty()));
	}

}
