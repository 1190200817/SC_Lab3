package Assist;

/**
 * 课程
 * Immutable
 * @author 刘小川
 *
 */
public class Course implements Comparable<Course>{
	private final int cid;
	private final String courseName;
	private final String teacherName;
	private final String location;
	
	// Abstraction function:
    //  cid表示课程id，courseName表示课程名，teacherNumber表示课程授课教师名，location表示上课地点
    // Representation invariant:
    //  courseName,teacherName,location都不能为空串
    // Safety from rep exposure:
    //  属性均使用了private和final进行修饰，并且均为不可变类型
	
	public Course(int cid, String courseName, String teacherName, String location) {
		this.cid = cid;
		this.courseName = courseName;
		this.teacherName = teacherName;
		this.location = location;
		checkRep();
	}

	private void checkRep() {
		assert !courseName.equals("");
		assert !teacherName.equals("");
		assert !location.equals("");
	}
	
	/**
	 * 获得课程ID
	 * @return 课程ID
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * 获得课程名
	 * @return 课程名
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * 获得授课教师名
	 * @return 教师名
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * 获得上课地点信息
	 * @return 上课地点
	 */
	public String getLocation() {
		return location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Course))
			return false;
		Course c = (Course) obj;
		return this.cid == c.cid && this.courseName.equals(c.courseName) && this.teacherName.equals(c.teacherName) && this.location.equals(c.location);
	}
	
	@Override
	public int hashCode() {
		return this.cid + this.courseName.hashCode() + this.teacherName.hashCode() + this.location.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Course");
		sb.append(cid + ":");
		sb.append(courseName);
		sb.append(",Teacher:");
		sb.append(teacherName);
		sb.append(",Place:");
		sb.append(location);
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public int compareTo(Course o) {
		if((this.cid - o.cid) != 0)
			return this.cid - o.cid;
		if(this.courseName.compareTo(o.courseName) != 0)
			return this.courseName.compareTo(o.courseName);
		if(this.teacherName.compareTo(o.teacherName) != 0)
			return this.teacherName.compareTo(o.teacherName);
		return this.location.compareTo(o.location);
	}

}
