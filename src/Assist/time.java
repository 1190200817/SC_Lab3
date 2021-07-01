package Assist;

/**
 * ʱ���
 * Immutable
 * @author ��С��
 *
 */
public class time implements Comparable<time>{
	private final long start;
	private final long end;
	
	// Abstraction function:
    //  start����һ��ʱ��εĿ�ʼ��end����һ��ʱ��εĽ���
    // Representation invariant:
    // start < end
	// start >= 0
    // Safety from rep exposure:
    //  ����ʹ����private��final���������Σ���Ϊ���ɱ�����
	
	private void checkRep() {
		assert start < end;
		assert start >= 0;
	}
	
	/**
	 * Constructor
	 * 
	 * @param start ʱ��εĿ�ʼʱ�䣬��Ϊ�Ǹ���
	 * @param end ʱ��εĽ���ʱ�䣬�Ҵ���start
	 */
	public time(long start, long end){
		this.start = start;
		this.end = end;
		checkRep();
	}
	
	/**
	 * ���ʱ��εĿ�ʼʱ��
	 * @return ʱ��εĿ�ʼʱ��
	 */
	public long getStart() {
		return start;
	}
	
	/**
	 * ���ʱ��εĽ���ʱ��
	 * @return ʱ��εĽ���ʱ��
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
