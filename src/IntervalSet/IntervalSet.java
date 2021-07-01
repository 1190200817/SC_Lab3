package IntervalSet;

import java.util.Set;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * A mutable set of labeled intervals, where each unique label is associated with a half-open interval [start,end)
 * which can be overlapped. Labels are of immutable type L and must implement the Object contract: 
 * they are compared for equality using Object.equals(java.lang.Object).
 * 
 * For example, { "A"=[0,10), "B"=[20,30) } is an interval set where the labels are Strings "A" and "B". 
 * We could add "D"=[25,35) although that interval overlaps with "B"=[20,30).
 * 注意和原来给出的ADT不同，这里允许区间Overlap!!!
 * @author 刘小川
 * 
 * @param <L> type of labels in this set, must be immutable
 */
public interface IntervalSet<L> {
	
	/**
	 * Create an empty interval set.
	 * 
	 * @param <L> type of labels of the interval set, must be immutable
	 * @return a new empty interval set
	 */
	public static <L> IntervalSet<L> empty(){
		return new CommonIntervalSet<L>();
	}
	
	/**
	 * Add a labeled interval (if not present) to this set, if it does not conflict
	 * with existing intervals.
	 * 
	 * Labeled intervals conflict if: they have the same label with different
	 * intervals.
	 * 
	 * For example, if this set is { "A"=[0,10), "B"=[20,30) }, 
	 * 	insert("A"=[0,10)) has no effect 
	 *  insert("B"=[10,20)) throws IntervalConflictException
	 *  insert("C"=[20,30)) adds "C"=[20,30)
	 *  insert("D"=[30,40)) adds "D"=[30,40)
	 * 
	 * @param start low end of the interval, inclusive, must be nonnegative
	 * @param end   high end of the interval, exclusive, must be greater than start
	 * @param label label to add
	 * @throws IntervalConflictException if label is already in this set and its
	 *                                   interval is not [start,end)
	 *         StartNegativeException if start is negative
	 */
	public void insert(long start,long end, L label) throws IntervalConflictException, StartNegativeException;
	
	/**
	 * Get the labels in this set.
	 * 
	 * @return the labels in this set
	 */
	public Set<L> labels();
	
	/**
	 * Remove a labeled interval from this set, if present.
	 * 
	 * @param label to remove
	 * @return true if this set contained label, and false otherwise
	 */
	public boolean remove(L label);
	
	/**
	 * Get the start of an interval.
	 * 
	 * @param label the label
	 * @return low end, inclusive, of the interval associated with label if label is present, else -1
	 */
	public long start (L label);
	
	/**
	 * Get the end of an interval.
	 * 
	 * @param label the label
	 * @return high end, exclusive, of the interval associated with label if label is present, else -1
	 */
	public long end (L label);
	
	/**
	 * make a copy of this IntervalSet
	 * @return the copy of this IntervalSet
	 */
	public IntervalSet<L> copy();
}
