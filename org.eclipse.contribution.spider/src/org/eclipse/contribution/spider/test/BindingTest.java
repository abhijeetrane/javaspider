package org.eclipse.contribution.spider.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.Drawing;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IFigureListener;
import org.eclipse.contribution.minidraw.IGroup;
import org.eclipse.contribution.spider.BindingFigure;
import org.eclipse.contribution.spider.ObjectFigure;
import org.eclipse.contribution.spider.SpiderView;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


// Functional tests of the Spider
// I'm thinking in their current form they're a bit fragile
// I'd like to move to something like FIT
public class BindingTest extends TestCase implements IFigureListener {
	private SpiderView view;
	private int changedCount;
	private Rectangle changedArea;
	
	public BindingTest(String name) {
		super(name);
	}
	
	public void setUp() {
		view= new SpiderView(new TestRuler() {public Point measure(String s) { return new Point(5, 5);}});
		changedCount= 0;
		changedArea= null;
	}

	public void xtestBindingUpdate() {
		IGroup subject= new Drawing();
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		IAction item= null;
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("figures")) { 
				item= each;
				break;
			}
		}
		item.run();
		Iterator figures= view.getDrawing().frontToBack();
		BindingFigure figureOfBinding= (BindingFigure) figures.next(); // Just as bad as an instanceof. Sigh...
		ObjectFigure figureOfArrayList= (ObjectFigure) figures.next(); // Need way to access
		view.getDrawing().addListener(this);
		figureOfArrayList.move(1, 1);
		assertEquals(4, changedCount);
		Rectangle expected= figureOfArrayList.bounds();
		expected.add(figureOfBinding.bounds());
		assertEquals(expected, changedArea);
	}
	
	public void testBindingDependencies() {
		IGroup subject= new Drawing();
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		IAction item= null;
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("figures")) { 
				item= each;
				break;
			}
		}
		item.run();
		Iterator figures= view.getDrawing().frontToBack();
		BindingFigure binding= (BindingFigure) figures.next(); // Just as bad as an instanceof. Sigh...
		IFigure to= (IFigure) figures.next();
		IFigure from= (IFigure) figures.next();
		binding.addListener(this);
		to.move(0, 0);
		assertEquals(2, changedCount); // Before and after the move
		from.move(0, 0);
		assertEquals(4, changedCount);
	}
	
	public void changed(IFigure figure) {
		changedCount++;
		changedArea= (changedArea == null)
			? figure.bounds()
			: changedArea.union(figure.bounds());
	}

}
