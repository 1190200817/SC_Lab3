package LabelTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Assist.Employee;
import Assist.Position;

class employeeTest {

	// 测试get类方法
	@Test
	public void employeeGetTest() {
		Employee e = new Employee("刘", Position.SECRETARY, 12345);
		assertEquals("刘", e.getName());
		assertEquals(Position.SECRETARY, e.getPos());
		assertEquals(12345, e.getPhoneNumber());
	}
	
	// 测试equals方法
	@Test
	public void employeeEqualTest() {
		Employee e0 = new Employee("刘", Position.VICEDEAN, 12345);
		Employee e1 = new Employee("刘", Position.VICEDEAN, 12345);
		Employee e2 = new Employee("李", Position.VICEDEAN, 45678);
		Employee e3 = new Employee("张", Position.VICEDEAN, 67890);
		assertTrue(e1.equals(e0));
		assertTrue(e0.equals(e1));
		
		assertFalse(e0.equals(e2));
		assertFalse(e0.equals(e3));
		assertFalse(e2.equals(e3));
		
		assertFalse(e0.equals(null));
	}
	
	// 测试hashCode方法
	@Test
	public void employeeHashCodeTest() {
		Employee e0 = new Employee("刘", Position.VICEDEAN, 12345);
		assertEquals("刘".hashCode() + 12345 + Position.VICEDEAN.hashCode(), e0.hashCode());
	}
	
	// 测试toString方法
	@Test
	public void employeeToStringTest() {
		Employee e0 = new Employee("刘", Position.VICEDEAN, 12345);
		String res = "[Employee:刘,Position:VICEDEAN,PhoneNumber:12345]";
		assertEquals(res, e0.toString());
	}
}
