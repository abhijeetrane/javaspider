package org.eclipse.contribution.spider.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

// Make sure reflection works the way I think it does
public class ReflectionTest extends TestCase {
	private Object test;
	
	public ReflectionTest(String name) {
		super(name);
	}
	
	public void testAccess() throws NoSuchFieldException, IllegalAccessException {
		Field f= this.getClass().getDeclaredField("test");
		f.setAccessible(true);
		Object result= f.get(this);
		assertSame(result, test);
	}
	
	public void testAttributes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Object subject= new PrivateClass();
		Method m= subject.getClass().getDeclaredMethod("reallyPrivateMethod", new Class[] {});
		m.setAccessible(true);
		m.invoke(subject, new Object[] {});
	}
	
	class PrivateClass {
		private void reallyPrivateMethod() {
		}
	}
	
	public void testReturnType() throws NoSuchMethodException, NoSuchMethodException {
		Method m= this.getClass().getDeclaredMethod("voidMethod", new Class[] {});
		assertEquals(Void.TYPE, m.getReturnType());
	}
	
	private void voidMethod() {
	}
}
