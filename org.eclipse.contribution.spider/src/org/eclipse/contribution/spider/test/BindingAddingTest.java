package org.eclipse.contribution.spider.test;

import java.util.ArrayList;
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
public class BindingAddingTest extends TestCase implements IFigureListener {
	private SpiderView view;
	private int changedCount;
	private Rectangle changedArea;
	private List changedFigures;
	
	public BindingAddingTest(String name) {
		super(name);
	}
	
	public void setUp() {
		view= new SpiderView(new TestRuler() {public Point measure(String s) { return new Point(5, 5);}});
		changedCount= 0;
		changedArea= null;
		changedFigures= new ArrayList();
	}

	public void testBindingChangedWhenAddedToDrawing() {
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
		view.getDrawing().addListener(this);
		item.run();
		IFigure binding= (BindingFigure) view.getDrawing().frontToBack().next();
		assertTrue(changedFigures.contains(binding));
	}
	
	public void changed(IFigure figure) {
		changedFigures.add(figure);
		changedCount++;
		changedArea= (changedArea == null)
			? figure.bounds()
			: changedArea.union(figure.bounds());
	}

}
