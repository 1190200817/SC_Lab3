package Assist;

public class Employee {
	private final String name;
	private final Position pos;
	private final long phoneNumber;
	
	// Abstraction function:
    //  name��ʾԱ�����֣�pos��ʾԱ��ְλ��phoneNumber��ʾԱ���绰����
    // Representation invariant:
    //  name��Ϊ�մ�
    // Safety from rep exposure:
    //  ���Ծ�ʹ����private��final�������Σ����Ҿ�Ϊ���ɱ�����
	
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
	 * ���Ա������
	 * @return ����
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���Ա��ְλ
	 * @return ְλ
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * ���Ա���绰����
	 * @return �绰����
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
