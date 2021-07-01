package Assist;

/**
 * 时间段
 * Immutable
 * @author 刘小川
 *
 */
public class time implements Comparable<time>{
	private final long start;
	private final long end;
	
	// Abstraction function:
    //  start代表一个时间段的开始，end代表一个时间段的结束
    // Representation invariant:
    // start < end
	// start >= 0
    // Safety from rep exposure:
    //  属性使用了private和final进行了修饰，且为不可变类型
	
	private void checkRep() {
		assert start < end;
		assert start >= 0;
	}
	
	/**
	 * Constructor
	 * 
	 * @param start 时间段的开始时间，且为非负数
	 * @param end 时间段的结束时间，且大于start
	 */
	public time(long start, long end){
		this.start = start;
		this.end = end;
		checkRep();
	}
	
	/**
	 * 获得时间段的开始时间
	 * @return 时间段的开始时间
	 */
	public long getStart() {
		return start;
	}
	
	/**
	 * 获得时间段的结束时间
	 * @return 时间段的结束时间
	 */
	public long getEnd() {
		return end;
	}
	
	@Override
	public int compareTo(time o) {
		if(this.start > o.getStart())
			return 1;
		if(this.start < o.getStart())
			return -1;
		return 0;
	}
	
	@Override
	public int hashCode() {
		return (int) (start + end);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof time))
			return false;
		time t = (time) obj;
		return start == t.start && end == t.end;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("time:");
		sb.append("[");
		sb.append(String.valueOf(start));
		sb.append(":");
		sb.append(String.valueOf(end));
		sb.append(")");
 		return sb.toString();
	}
}
