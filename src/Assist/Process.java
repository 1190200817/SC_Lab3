package Assist;

/**
 * 进程
 * Immutable
 * @author 刘小川
 *
 */
public class Process {
	
	private final int pid;
	private final String name;
	private final long minTime;
	private final long maxTime;
	
	// Abstraction function:
    //  pid表示进程id，name表示进程名称，minTime表示最短执行时间，maxTime表示最长执行时间
    // Representation invariant:
    //  name不能为空串
	// maxTime >=  minTime
    // Safety from rep exposure:
    //  属性均使用了private和final进行修饰，并且均为不可变类型
	
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
	 * 获得进程ID
	 * @return 进程ID
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * 获得进程名称
	 * @return 进程名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获得进程最短执行时间
	 * @return 最短执行时间
	 */
	public long getMinimumTime() {
		return minTime;
	}

	/**
	 * 获得进程最长执行时间
	 * @return 最长执行时间
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
