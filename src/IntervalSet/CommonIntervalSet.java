package IntervalSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Assist.time;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * IntervalSet<L>的一个实现类
 * @author 刘小川
 *
 * @param <L>
 */
public class CommonIntervalSet<L> implements IntervalSet<L> {

	private final Map<L, time> intervalMap = new HashMap<>(); 
	
	// Abstraction function:
    //  intervalMap代表一个时间段的映射，每个键表示一个标签，对应的值是相应的时间段
    // Representation invariant:
    //  每一个时间段的结束时间大于开始时间
	//  每个时间段的开始时间是非负数
    // Safety from rep exposure:
    //  属性使用了private和final进行了修饰，且没有返回属性的方法
	
	/**
	 * Constructor
	 */
	public CommonIntervalSet() {
		checkRep();
	}

	private void checkRep() {
		for(L label:intervalMap.keySet()) {
			time t = intervalMap.get(label);
			assert t.getStart() < t.getEnd();
			assert t.getStart() >= 0;
		}
	}
	
	@Override
	public void insert(long start, long end, L label) throws IntervalConflictException, StartNegativeException{
		if(start < 0)
			throw new StartNegativeException();
		if(start >= end){
			return;
		}
		if(intervalMap.containsKey(label)) {
			if(start(label) == start && end(label) == end)
				return;
			else {
				throw new IntervalConflictException("insert error: label conflict");
			}
		}
		else {
			time t = new time(start, end);
			intervalMap.put(label, t);
		}
		checkRep();		
	}

	@Override
	public Set<L> labels() {
		Set<L> res = new HashSet<>();
		for(L key:intervalMap.keySet()) {
			res.add(key);
		}
		return res;
	}

	@Override
	public boolean remove(L label) {
		if(!intervalMap.containsKey(label))
			return false;
		intervalMap.remove(label);
		checkRep();
		return true;
	}

	@Override
	public long start(L label) {
		if(!intervalMap.containsKey(label))
			return -1;
		return intervalMap.get(label).getStart();
	}

	@Override
	public long end(L label) {
		if(!intervalMap.containsKey(label))
			return -1;
		return intervalMap.get(label).getEnd();
	}

	@Override
	public IntervalSet<L> copy() {
		checkRep();
		IntervalSet<L> res = new CommonIntervalSet<>();
		for(L key:intervalMap.keySet()) {
			time t = intervalMap.get(key);
			try {
				res.insert(t.getStart(), t.getEnd(), key);
			} catch (IntervalConflictException e) {
				
			} catch (StartNegativeException e) {
				
			}
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(L l:intervalMap.keySet()) {
			sb.append(l.toString());
			sb.append(",");
			sb.append(intervalMap.get(l).toString());
			sb.append(";");
		}
		return sb.toString();
	}
}


