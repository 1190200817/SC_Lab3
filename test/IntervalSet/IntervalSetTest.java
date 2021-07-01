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
    
    // 测试insert函数
    @Test
    public void insertTest() {
    	// 正常情况
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	try {
			inset.insert(0, 3, e1);
			assertEquals(0, inset.start(e1));
			assertEquals(3, inset.end(e1));
			assertEquals(true, inset.labels().contains(e1));
			assertEquals(1, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
        
    	// 标签重复，但时间段一致
    	Employee e2 = new Employee("刘", Position.SECRETARY, 123456);
    	try {
			inset.insert(0, 3, e2);
			assertEquals(0, inset.start(e2));
			assertEquals(3, inset.end(e2));
			assertEquals(true, inset.labels().contains(e2));
			assertEquals(1, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// 标签重复，时间段冲突
    	boolean flag = false;
    	try {
			inset.insert(2, 4, e2);
		} catch (IntervalConflictException | StartNegativeException e) {
			flag = true;
		}
    	finally {
			assertEquals(true, flag);
		}
    	
    	// 多个标签
    	Employee e3 = new Employee("李", Position.VICEDEAN, 654321);
    	try {
			inset.insert(1, 4, e3);
			assertEquals(1, inset.start(e3));
			assertEquals(4, inset.end(e3));
			assertEquals(true, inset.labels().contains(e3));
			assertEquals(2, inset.labels().size());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// start >= end
    	Employee e4 = new Employee("王", Position.VICEDEAN, 785621);
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
    
    // 测试labels函数
    @Test
    public void labelsTest() {
        // 添加成功
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	Set<Employee> res = new HashSet<>();
    	try {
			inset.insert(0, 3, e1);
			res.add(e1);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// 添加失败
    	Employee e2 = new Employee("李", Position.SECRETARY, 123456);
    	try {
			inset.insert(3, 3, e2);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// 删除失败
    	inset.remove(e2);
    	assertEquals(res, inset.labels());
    	
    	// 删除成功
    	inset.remove(e1);
    	res.remove(e1);
    	assertEquals(res, inset.labels());
    	
    	// 多个标签
    	Employee e3 = new Employee("张", Position.SECRETARY, 123456);
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
    
    // 测试remove函数
    @Test 
    public void removeTest() {
    	// 删除失败
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertFalse(inset.remove(e1));
    	
    	Set<Employee> res = new HashSet<>();
    	try {
			inset.insert(0, 3, e1);
			res.add(e1);
			assertEquals(res, inset.labels());
		} catch (IntervalConflictException | StartNegativeException e) {
			
		}
    	
    	// 删除成功
    	assertTrue(inset.remove(e1));
    	assertEquals(0, inset.labels().size());
    	
    	// 多个标签删除
    	Employee e2 = new Employee("李", Position.SECRETARY, 123456);
    	Employee e3 = new Employee("张", Position.SECRETARY, 123456);
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
    
    // 测试start函数
    @Test
    public void startTest() {
    	// 标签不存在
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertEquals(-1, inset.start(e1));
    	
    	// 标签存在
    	try {
			inset.insert(0, 3, e1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	assertEquals(0, inset.start(e1));
    }
    
    // 测试end函数
    @Test
    public void endTest() {
    	// 标签不存在
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	assertEquals(-1, inset.end(e1));
    	
    	// 标签存在
    	try {
			inset.insert(0, 3, e1);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	assertEquals(3, inset.end(e1));
    }
    
    // 测试copy函数
    @Test
    public void copyTest() {
    	Employee e1 = new Employee("刘", Position.SECRETARY, 123456);
    	Employee e2 = new Employee("王", Position.SECRETARY, 123456);
    	Employee e3 = new Employee("张", Position.SECRETARY, 123456);
    	IntervalSet<Employee> inset = IntervalSet.empty();
    	try {
			inset.insert(1, 2, e1);
			inset.insert(2, 4, e2);
			inset.insert(3, 4, e3);
		} catch (IntervalConflictException | StartNegativeException e) {
		}
    	IntervalSet<Employee> insetCopy = inset.copy();
    	// copy的任何结果相同
    	assertEquals(inset.labels(), insetCopy.labels());
    	assertEquals(inset.start(e2), insetCopy.start(e2));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.remove(e3), insetCopy.remove(e3));
    	assertEquals(inset.end(e3), insetCopy.end(e3));
    	assertEquals(inset.labels(), insetCopy.labels());
    }
}
