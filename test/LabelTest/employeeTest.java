package LabelTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Assist.Employee;
import Assist.Position;

class employeeTest {

	// ����get�෽��
	@Test
	public void employeeGetTest() {
		Employee e = new Employee("��", Position.SECRETARY, 12345);
		assertEquals("��", e.getName());
		assertEquals(Position.SECRETARY, e.getPos());
		assertEquals(12345, e.getPhoneNumber());
	}
	
	// ����equals����
	@Test
	public void employeeEqualTest() {
		Employee e0 = new Employee("��", Position.VICEDEAN, 12345);
		Employee e1 = new Employee("��", Position.VICEDEAN, 12345);
		Employee e2 = new Employee("��", Position.VICEDEAN, 45678);
		Employee e3 = new Employee("��", Position.VICEDEAN, 67890);
		assertTrue(e1.equals(e0));
		assertTrue(e0.equals(e1));
		
		assertFalse(e0.equals(e2));
		assertFalse(e0.equals(e3));
		assertFalse(e2.equals(e3));
		
		assertFalse(e0.equals(null));
	}
	
	// ����hashCode����
	@Test
	public void employeeHashCodeTest() {
		Employee e0 = new Employee("��", Position.VICEDEAN, 12345);
		assertEquals("��".hashCode() + 12345 + Position.VICEDEAN.hashCode(), e0.hashCode());
	}
	
	// ����toString����
	@Test
	public void employeeToStringTest() {
		Employee e0 = new Employee("��", Position.VICEDEAN, 12345);
		String res = "[Employee:��,Position:VICEDEAN,PhoneNumber:12345]";
		assertEquals(res, e0.toString());
	}
}
