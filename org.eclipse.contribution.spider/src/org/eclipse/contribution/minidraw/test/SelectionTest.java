package org.eclipse.contribution.minidraw.test;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.Drawing;
import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.IGroup;
import org.eclipse.contribution.minidraw.SelectionDrawing;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class SelectionTest extends TestCase {

	public SelectionTest(String name) {
		super(name);
	}

	public void testAdd() {
		IGroup d= new Drawing();
		SelectionDrawing sd= new SelectionDrawing(d, null);
		Figure f= new Figure() {
			public void paint(GC gc) {}
			public Rectangle bounds() {return null;}
		};
		sd.add(f);
		assertEquals(f, d.frontToBack().next());
		sd.remove(f);
		assertTrue(! d.frontToBack().hasNext());
	}
	
	

}
