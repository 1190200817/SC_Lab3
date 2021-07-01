package Assist;

/**
 * �γ�
 * Immutable
 * @author ��С��
 *
 */
public class Course implements Comparable<Course>{
	private final int cid;
	private final String courseName;
	private final String teacherName;
	private final String location;
	
	// Abstraction function:
    //  cid��ʾ�γ�id��courseName��ʾ�γ�����teacherNumber��ʾ�γ��ڿν�ʦ����location��ʾ�Ͽεص�
    // Representation invariant:
    //  courseName,teacherName,location������Ϊ�մ�
    // Safety from rep exposure:
    //  ���Ծ�ʹ����private��final�������Σ����Ҿ�Ϊ���ɱ�����
	
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
	 * ��ÿγ�ID
	 * @return �γ�ID
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * ��ÿγ���
	 * @return �γ���
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * ����ڿν�ʦ��
	 * @return ��ʦ��
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * ����Ͽεص���Ϣ
	 * @return �Ͽεص�
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
