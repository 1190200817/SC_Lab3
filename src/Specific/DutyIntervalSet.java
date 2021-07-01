package Specific;

import IntervalSet.IntervalSet;
import decorator.CommonNoBlankIntervalSet;
import decorator.NoBlankIntervalSet;
import decorator.NoOverlapIntervalSet;

/**
 * 针对值班表管理的IntervalSet
 * @author 刘小川
 *
 * @param <L>
 */
public class DutyIntervalSet<L> extends CommonNoBlankIntervalSet<L> implements NoBlankIntervalSet<L> {

	public DutyIntervalSet(long startTime, long endTime) {
		super(startTime, endTime, new NoOverlapIntervalSet<L>(IntervalSet.empty()));
	}

}
