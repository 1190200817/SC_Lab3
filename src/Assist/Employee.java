package Assist;

public class Employee {
	private final String name;
	private final Position pos;
	private final long phoneNumber;
	
	// Abstraction function:
    //  name表示员工名字，pos表示员工职位，phoneNumber表示员工电话号码
    // Representation invariant:
    //  name不为空串
    // Safety from rep exposure:
    //  属性均使用了private和final进行修饰，并且均为不可变类型
	
	public Employee(String name, Position pos, long phoneNumber) {
		this.name = name;
		this.pos = pos;
		this.phoneNumber = phoneNumber;
		checkRep();
	}
	
	private void checkRep() {
		assert name != "";
	}

	/**
	 * 获得员工名字
	 * @return 名字
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获得员工职位
	 * @return 职位
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * 获得员工电话号码
	 * @return 电话号码
	 */
	public long getPhoneNumber() {
		return phoneNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Employee))
			return false;
		Employee e = (Employee) obj;
		return name.equals(e.name) && pos.equals(e.pos) && phoneNumber == e.phoneNumber;
	}
	
	@Override
	public int hashCode() {
		return (int)(name.hashCode() + pos.hashCode() + phoneNumber);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Employee:");
		sb.append(name);
		sb.append(",Position:");
		sb.append(pos);
		sb.append(",PhoneNumber:");
		sb.append(phoneNumber);
		sb.append("]");
		return sb.toString();
	}
}
