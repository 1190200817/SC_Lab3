package MultiIntervalSet;

import java.util.Set;

import IntervalSet.IntervalSet;

import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * A mutable set of labeled intervals, where each label is associated with one
 * or more non-overlapping half-open intervals [start, end). Neither intervals
 * with the same label nor with different labels may overlap. 
 * 
 * Labels are of immutable type L and must implement the Object contract: they are 
 * compared for equality using Object.equals(java.lang.Object). 
 * 
 * For example, { * "A"=[[0,10)], "B"=[[20,30)] } is a multi-interval set where 
 * the labels are Strings "A" and "B". We could add "A"=[10,20) to that set to obtain 
 * {"A"=[[0,10),[10,20)], "B"=[[20,30)] }.
 * 
 * 	一个标签可对应多个时间段的IntervalSet
 * 	一个标签可以对应多个时间段，同一个标签的时间段相互之间不允许重叠
 * 	
 * PS2 instructions: this is a required ADT class. You may not change the
 * specifications or add new public methods. You must use IntervalSet in your
 * rep, but otherwise the implementation of this class is up to you.
 * 
 * @param <L> type of labels in this set, must be immutable
 */
public interface MultiIntervalSet<L> {
	
	/**
	 * Create an empty multi-interval set.
	 * 
	 * @param <L> type of labels of the multi-interval set, must be immutable
	 * @return a new empty multi-interval set
	 */
	public static <L> MultiIntervalSet<L> empty() {
		return new CommonMultiIntervalSet<L>();
	}
	
	/**
	 * 	用一个IntervalSet初始化MultiIntervalSet
	 * @param intervalSet 一个IntervalSet
	 * @return 初始化的MultiIntervalSet
	 */
	public static <L> MultiIntervalSet<L> init(IntervalSet<L> intervalSet) {
		return new CommonMultiIntervalSet<L>(intervalSet);
	}
	
	/**
	 * Add a labeled interval (if not present) to this set, if it does not conflict with existing intervals.
	 * 
	 * Labeled intervals conflict if:
	 * 		they have the same label with different, overlapping intervals, or
	 *		they have different labels with overlapping intervals.
	 * 
	 * For example, if this set is { "A"=[[0,10),[20,30)] },
	 *		insert("A"=[0,10)) has no effect
	 *		insert("B"=[10,20)) adds "B"=[[10,20)]
	 *		insert("C"=[20,30)) throws IntervalConflictException
	 *
	 *
	 * @param start low end of the interval, inclusive, must be nonnegative
	 * @param end high end of the interval, exclusive, must be greater than start
	 * @param label label to add
	 * @throws IntervalConflictException if label is already in this set and associated 
	 * 									 with an interval other than [start,end) that 
	 * 									 overlaps [start,end), or if an interval in this 
	 * 									 set with a different label overlaps [start,end)
	 * 		    StartNegativeException if start is negative
	 */
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException;
	
	/**
	 * Get the labels in this set.
	 * 
	 * @return the labels in this set
	 */
	public Set<L> labels();
	
	/**
	 * Remove all intervals of the given label from this set, if any.
	 * 
	 * @param label label to remove
	 * @return true if this set contained label, and false otherwise
	 */
	public boolean remove(L label);
	
	/**
	 * Get all the intervals in this set associated with a given label. The returned set has 
	 * Integer labels that act as indices: label 0 is associated with the lowest interval, 
	 * 1 the next, and so on, for all the intervals in this set that have the provided label.
	 * 
	 * For example, if this set is { "A"=[[0,10),[20,30)], "B"=[[10,20)] },
	 * 		intervals("A") returns { 0=[0,10), 1=[20,30) }
	 *	
	 * @param label the label
	 * @return a new interval set that associates integer indices with the in-order intervals 
	 * 		   of label in this set
	 */
	public IntervalSet<Integer> intervals(L label);
	
}
