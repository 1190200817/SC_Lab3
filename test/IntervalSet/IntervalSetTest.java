package IntervalSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Assist.Employee;
import Assist.Position;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for instance methods of IntervalSet.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain IntervalSet instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class IntervalSetTest {
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty IntervalSet of the particular implementation being tested
     */
    public abstract IntervalSet<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialLabelsEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new interval set to have no intervals",
                Collections.emptySet(), emptyInstance().labels());
    }
    
    // ����insert����
    @Test
    public void insertTest() {
    	// �������
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	try {
			inset.insert(0, 3, e1);
			assertEquals(0, inset.start(e1));
			assertEquals(3, inset.end(e1));
			assertEquals(true, inset.labels().contains(e1));
			assertEquals(1, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
        
    	// ��ǩ�ظ�����ʱ���һ��
    	Employee e2 = new Employee("��", Position.SECRETARY, 123456);
    	try {
			inset.insert(0, 3, e2);
			assertEquals(0, inset.start(e2));
			assertEquals(3, inset.end(e2));
			assertEquals(true, inset.labels().contains(e2));
			assertEquals(1, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// ��ǩ�ظ���ʱ��γ�ͻ
    	boolean flag = false;
    	try {
			inset.insert(2, 4, e2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
    	finally {
			assertEquals(true, flag);
		}
    	
    	// �����ǩ
    	Employee e3 = new Employee("��", Position.VICEDEAN, 654321);
    	try {
			inset.insert(1, 4, e3);
			assertEquals(1, inset.start(e3));
			assertEquals(4, inset.end(e3));
			assertEquals(true, inset.labels().contains(e3));
			assertEquals(2, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// start >= end
    	Employee e4 = new Employee("��", Position.VICEDEAN, 785621);
    	try {
			inset.insert(4, 3, e4);
			assertEquals(false, inset.labels().contains(e4));
			inset.insert(4, 4, e4);
			assertEquals(false, inset.labels().contains(e4));
			assertEquals(2, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// start < 0
    	flag = false;
    	try {
			inset.insert(-1, 3, e4);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
    	finally {
			assertTrue(flag);
		}
    }
    
    // ����labels����
    @Test
    public void labelsTest() {
        // ��ӳɹ�
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	Set<Employee> res = new HashSet<>();
    	try {
			inset.insert(0, 3, e1);
			res.add(e1);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// ���ʧ��
    	Employee e2 = new Employee("��", Position.SECRETARY, 123456);
    	try {
			inset.insert(3, 3, e2);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// ɾ��ʧ��
    	inset.remove(e2);
    	assertEquals(res, inset.labels());
    	
    	// ɾ���ɹ�
    	inset.remove(e1);
    	res.remove(e1);
    	assertEquals(res, inset.labels());
    	
    	// �����ǩ
    	Employee e3 = new Employee("��", Position.SECRETARY, 123456);
    	try {
			inset.insert(1, 2, e1);
			inset.insert(2, 4, e2);
			inset.insert(3, 4, e3);
			res.add(e1);
			res.add(e2);
			res.add(e3);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    }
    
    // ����remove����
    @Test 
    public void removeTest() {
    	// ɾ��ʧ��
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertFalse(inset.remove(e1));
    	
    	Set<Employee> res = new HashSet<>();
    	try {
			inset.insert(0, 3, e1);
			res.add(e1);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// ɾ���ɹ�
    	assertTrue(inset.remove(e1));
    	assertEquals(0, inset.labels().size());
    	
    	// �����ǩɾ��
    	Employee e2 = new Employee("��", Position.SECRETARY, 123456);
    	Employee e3 = new Employee("��", Position.SECRETARY, 123456);
    	try {
			inset.insert(1, 2, e1);
			inset.insert(2, 4, e2);
			inset.insert(3, 4, e3);
			res.add(e1);
			res.add(e2);
			res.add(e3);
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	assertEquals(3, inset.labels().size());
    	assertTrue(inset.remove(e2));
    	assertEquals(2, inset.labels().size());
    	assertFalse(inset.remove(e2));
    	assertTrue(inset.remove(e3));
    	assertEquals(1, inset.labels().size());
    	assertTrue(inset.remove(e1));
    	assertEquals(0, inset.labels().size());
    	
    }
    
    // ����start����
    @Test
    public void startTest() {
    	// ��ǩ������
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertEquals(-1, inset.start(e1));
    	
    	// ��ǩ����
    	try {
			inset.insert(0, 3, e1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	assertEquals(0, inset.start(e1));
    }
    
    // ����end����
    @Test
    public void endTest() {
    	// ��ǩ������
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertEquals(-1, inset.end(e1));
    	
    	// ��ǩ����
    	try {
			inset.insert(0, 3, e1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	assertEquals(3, inset.end(e1));
    }
    
    // ����copy����
    @Test
    public void copyTest() {
    	Employee e1 = new Employee("��", Position.SECRETARY, 123456);
    	Employee e2 = new Employee("��", Position.SECRETARY, 123456);
    	Employee e3 = new Employee("��", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	try {
			inset.insert(1, 2, e1);
			inset.insert(2, 4, e2);
			inset.insert(3, 4, e3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	IntervalSet<Employee> insetCopy = inset.copy();
    	// copy���κν����ͬ
    	assertEquals(inset.labels(), insetCopy.labels());
    	assertEquals(inset.start(e2), insetCopy.start(e2));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.remove(e3), insetCopy.remove(e3));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.labels(), insetCopy.labels());
    }
}
