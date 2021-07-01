package Assist;

/**
 * ����
 * Immutable
 * @author ��С��
 *
 */
public class Process {
	
	private final int pid;
	private final String name;
	private final long minTime;
	private final long maxTime;
	
	// Abstraction function:
    //  pid��ʾ����id��name��ʾ�������ƣ�minTime��ʾ���ִ��ʱ�䣬maxTime��ʾ�ִ��ʱ��
    // Representation invariant:
    //  name����Ϊ�մ�
	// maxTime >=  minTime
    // Safety from rep exposure:
    //  ���Ծ�ʹ����private��final�������Σ����Ҿ�Ϊ���ɱ�����
	
	public Process(int pid, String name, long minimumTime, long maximumTime) {
		this.pid = pid;
		this.name = name;
		this.maxTime = maximumTime;
		this.minTime = minimumTime;
		checkRep();
	}

	private void checkRep() {
		assert !name.equals("");
		assert maxTime >= minTime;
	}
	
	/**
	 * ��ý���ID
	 * @return ����ID
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * ��ý�������
	 * @return ��������
	 */
	public String getName() {
		return name;
	}

	/**
	 * ��ý������ִ��ʱ��
	 * @return ���ִ��ʱ��
	 */
	public long getMinimumTime() {
		return minTime;
	}

	/**
	 * ��ý����ִ��ʱ��
	 * @return �ִ��ʱ��
	 */
	public long getMaximumTime() {
		return maxTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Process))
			return false;
		return this.pid == ((Process)obj).pid;
	}
	
	@Override
	public int hashCode() {
		return this.pid;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Process");
		sb.append(pid + ":");
		sb.append(name);
		sb.append(",minTime:");
		sb.append(minTime);
		sb.append(",maxTime:");
		sb.append(maxTime);
		sb.append("]");
		return sb.toString();
	}
	
}
