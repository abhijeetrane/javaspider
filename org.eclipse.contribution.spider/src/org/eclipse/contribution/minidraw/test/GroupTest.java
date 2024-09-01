package org.eclipse.contribution.minidraw.test;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.Group;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IFigureListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class GroupTest extends TestCase implements IFigureListener {
	int changedCount= 0;
	
	public GroupTest(String name) {
		super(name);
	}
	
	public void testChangedOnAdd() {
		IFigure figure= makeFigure();
		Group group= new Group();
		group.addListener(this);
		group.add(figure);
		// Once for the figure, and before and after the add for the group
		assertEquals(3, changedCount);
	}
	
	public void testChangedOnDelete() {
		IFigure figure= makeFigure();
		Group group= new Group();
		group.add(figure);
		group.addListener(this);
		group.remove(figure);
		// Once for the figure, and before and after the add for the group
		assertEquals(3, changedCount);
		assertTrue(! group.anyOrder().hasNext());
		assertTrue(! figure.getListeners().hasNext());
	}
	
	public void changed(IFigure figure) {
		changedCount++; // It'd be nice to see that the bounds are correct
	}

	public IFigure makeFigure() {
		return new Figure() {
			public void paint(GC gc) {}; 
			public Rectangle bounds() {return null;}
		};
	}

}
