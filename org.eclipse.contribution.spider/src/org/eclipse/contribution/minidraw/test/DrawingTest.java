package org.eclipse.contribution.minidraw.test;

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.Drawing;
import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IFigureListener;
import org.eclipse.contribution.minidraw.IGroup;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class DrawingTest extends TestCase implements IFigureListener {
	int changedCount= 0;
	
	public DrawingTest(String name) {
		super(name);
	}
	
	public void testIterators() {
		Drawing d= new Drawing();
		IFigure figure1= makeFigure();
		IFigure figure2= makeFigure();
		d.add(figure1);
		d.add(figure2);
		Iterator front= d.frontToBack();
		Iterator back= d.backToFront();
		assertSame(figure2, front.next());
		assertSame(figure1, back.next());
		assertSame(figure1, front.next());
		assertSame(figure2, back.next());
		assertTrue(! front.hasNext());
		assertTrue(! back.hasNext());
	}
	
	public void testChangedOnAdd() {
		IGroup d= new Drawing();
		IFigure figure= makeFigure();
		d.addListener(this);
		d.add(figure);
		assertEquals(1, changedCount);
	}
	
	public void testChangedOnDelete() {
		IFigure figure= makeFigure();
		IGroup drawing= new Drawing();
		drawing.add(figure);
		drawing.addListener(this);
		drawing.remove(figure);
		assertEquals(1, changedCount);
		assertTrue(! drawing.anyOrder().hasNext());
		assertTrue(! figure.getListeners().hasNext());
	}
	
	public void changed(IFigure figure) {
		changedCount++; // It'd be nice to see that the bounds are correct
	}

	public IFigure makeFigure() {
		return new Figure() {public void paint(GC gc) {}; public Rectangle bounds() {return null;}};
	}

}
