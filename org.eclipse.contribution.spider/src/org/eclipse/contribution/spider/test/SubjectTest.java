package org.eclipse.contribution.spider.test;

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.contribution.spider.reflect.IndexedField;
import org.eclipse.contribution.spider.reflect.NamedField;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.swt.graphics.Point;

public class SubjectTest extends TestCase {
	public SubjectTest(String name) {
		super(name);
	}
	
	public void testRawSubject() {
		Point rawSubject= new Point(2, 3);
		RawSubject subject= RawSubject.create(rawSubject);
		assertEquals("Point", subject.getClassName());
		int count= 0;
		for (Iterator all= subject.getFields(); all.hasNext(); count++) {
			NamedField each= (NamedField) all.next();
			assertTrue(each.isPrimitive());
			RawSubject value= (RawSubject) each.getValue(); // We can do this because we're testing
			if (each.getName().equals("x")) assertEquals(new Integer(2), value.getRawSubject());
			if (each.getName().equals("y")) assertEquals(new Integer(3), value.getRawSubject());
		}
		assertEquals(2, count);
		assertEquals(rawSubject, subject.getRawSubject());
		assertTrue(! new RawSubject(new Point(2, 3)).equals(subject));
		assertEquals(new RawSubject(subject.getRawSubject()), subject);
	}
	
	class Super {
		public int attribute() {
			return 0;
		}
	}
	class Sub extends Super {
		public int attribute() {
			return 0;
		}
	}
	public void testNoDuplicateAttributes() {
		Object rawSubject= new Sub();
		RawSubject subject= RawSubject.create(rawSubject);
		int count= 0;
		for (Iterator all= subject.getAttributes(); all.hasNext(); all.next(), count++) {
		}
		assertEquals(4 /*From Object*/ + 1 /*From Sub, no duplication*/, count);
	}
	
	public void testArraySubject() {
		String[] rawSubject= new String[] {"one", "two"}; 
		RawSubject subject= RawSubject.create(rawSubject);
		assertEquals("String[]", subject.getClassName());
		int count= 0;
		for (Iterator all= subject.getFields(); all.hasNext(); count++) {
			IndexedField each= (IndexedField) all.next();
			assertTrue(! each.isPrimitive());
			RawSubject value= (RawSubject) each.getValue();
			if (each.getName().equals("0")) assertEquals("one", value.getRawSubject());
			if (each.getName().equals("1")) assertEquals("two", value.getRawSubject());
		}
		assertEquals(2, count);
	}
}
 