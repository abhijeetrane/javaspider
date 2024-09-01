package org.eclipse.contribution.spider.test;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ObjectFigure;
import org.eclipse.contribution.spider.reflect.IndexedField;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.swt.graphics.Point;

public class ObjectFigureTest extends TestCase {
	int changedCount= 0;
	
	public ObjectFigureTest(String name) {
		super(name);
	}
	
	public void testThatRemoveLaysOut() {
		IRuler ruler= new TestRuler() {public Point measure(String string) {return new Point(10, 10);}};
		Object[] subject= new Object[2];
		ObjectFigure figure= new ObjectFigure(new RawSubject(subject), ruler, new Point(0,0));
		IField zero= new IndexedField(subject, 0);
		IFigure fieldZero= figure.addField(zero, ruler);
		IField one= new IndexedField(subject, 1);
		IFigure fieldOne= figure.addField(one, ruler);
		assertEquals(20, fieldOne.bounds().y);
		figure.remove(fieldZero);
		assertEquals(10, fieldOne.bounds().y);
	}
	
}
