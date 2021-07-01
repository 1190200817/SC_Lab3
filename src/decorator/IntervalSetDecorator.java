package decorator;

import java.util.Set;

import IntervalSet.IntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * IntervalSet装饰器
 * @author 刘小川
 *
 * @param <L>
 */
public abstract class IntervalSetDecorator<L> implements IntervalSet<L> {
	
	protected final IntervalSet<L> intervalSet;
	
    // Abstraction function:
    //  intervalSet是需要修饰的IntervalSet类
    // Representation invariant:
    //  intervalSet的类型是IntervalSet
    // Safety from rep exposure:
    //  使用final修饰intervalSet
	//	没有方法会直接返回intervalSet，因此无需防御式拷贝
	
	/**
	 * Constructor
	 * 从外部传入IntervalSet进行初始化（使用Decorator设计模式进行复用）
	 * @param intervalSet 外部传入的需要修饰的IntervalSet
	 */
	public IntervalSetDecorator(IntervalSet<L> intervalSet) {
		this.intervalSet = intervalSet;
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException {
		intervalSet.insert(start, end, label);
	}
	
	@Override
	public Set<L> labels() {
		return intervalSet.labels();
	}
	
	@Override
	public boolean remove(L label) {
		return intervalSet.remove(label);
	}
	
	@Override
	public long start(L label) {
		return intervalSet.start(label);
	}
	
	@Override
	public long end(L label) {
		return intervalSet.end(label);
	}
	
}
