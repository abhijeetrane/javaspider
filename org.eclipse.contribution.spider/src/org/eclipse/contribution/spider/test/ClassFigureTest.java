package org.eclipse.contribution.spider.test;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.contribution.spider.ClassFigure;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ClassFigureTest extends TestCase {
	public ClassFigureTest(String name) {
		super(name);
	}
	
	public void testExtent() {
		IRuler r= new TestRuler() { 
			public Point measure(String s) {
				return new Point(20, 10);
			}
		};
		ClassFigure f= new ClassFigure(RawSubject.create(new Object()), r, new Point(5, 7));
		assertEquals(new Rectangle(5,7,20,10), f.bounds());
		assertEquals("Object", f.text()); 
	}
	
	public void testArrayText() {
		IRuler r= new TestRuler();
		ClassFigure f= new ClassFigure(RawSubject.create(new Object[] {}), r, new Point(0,0));
		assertEquals("Object[]", f.text());
	}
}
