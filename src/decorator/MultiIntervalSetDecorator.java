package decorator;

import java.util.Set;

import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * MultiIntervalSet装饰器
 * @author 刘小川
 *
 * @param <L>
 */
public abstract class MultiIntervalSetDecorator<L> implements MultiIntervalSet<L>{

	protected final MultiIntervalSet<L> multiIntervalSet;
	
	 // Abstraction function:
    //  multiIntervalSet是需要修饰的MultiIntervalSet类
    // Representation invariant:
    //  multiIntervalSet的类型是MultiIntervalSet
    // Safety from rep exposure:
    //  使用final修饰multiIntervalSet
	//	没有方法会直接返回multiIntervalSet，因此无需防御式拷贝
	
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
