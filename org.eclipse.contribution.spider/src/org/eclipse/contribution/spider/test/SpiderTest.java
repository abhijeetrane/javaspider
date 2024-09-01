package org.eclipse.contribution.spider.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.DeleteHandle;
import org.eclipse.contribution.minidraw.Drawing;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.spider.BindingFigure;
import org.eclipse.contribution.spider.ClassFigure;
import org.eclipse.contribution.spider.FieldFigure;
import org.eclipse.contribution.spider.ObjectFigure;
import org.eclipse.contribution.spider.SpiderView;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.Point;


// Functional tests of the Spider
// I'm thinking in their current form they're a bit fragile
// I'd like to move to something like FIT
public class SpiderTest extends TestCase {
	private SpiderView view;
	public SpiderTest(String name) {
		super(name);
	}
	
	public void setUp() {
		view= new SpiderView(new TestRuler() {public Point measure(String s) { return new Point(5, 5);}});
	}

	public void testSpider() {
		Object subject= new Object();
		view.exploreObject(subject);
		ObjectFigure figure= (ObjectFigure) view.getDrawing().frontToBack().next();
		assertEquals(RawSubject.create(subject), figure.getSubject());
	}

	public void testRemove() {
		Object subject= new Object();
		ObjectFigure first= view.exploreObject(subject);
		view.remove(RawSubject.create(subject));
		ObjectFigure second= view.exploreObject(subject);
		// Should return a different figure iff the objects map has been correctly updated
		assertTrue(first != second);
	}

	public void testExploreVariable() {
		Drawing subject= new Drawing();
		view.exploreObject(subject);
		ObjectFigure figure= view.getFigure(new RawSubject(subject), new Point(0, 0));
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
		assertTrue(figures.next() instanceof BindingFigure);
		assertTrue(figures.next() instanceof ObjectFigure);
	}

	public void testExplorePrimitiveVariable() {
		Object subject= new Point(2, 3);
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		IAction item= null;
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("x")) {
				item= each;
				break;
			}
		}
		item.run();
		FieldFigure field= (FieldFigure) figure.frontToBack().next(); // Need way to access
		assertEquals("x: 2", field.getText());
	}

	public void testExploreNullVariable() {
		Object subject= new Object[] {null};
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		IAction item= null;
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("0")) {
				item= each;
				break;
			}
		}
		item.run();
		FieldFigure field= (FieldFigure) figure.frontToBack().next(); // Need way to access
		assertEquals("0: null", field.getText());
	}

	public void testExploreStringArrayVariable() {
		Object subject= new String[] {"test"};
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("0")) {
				each.run();
				break;
			}
		}
		FieldFigure field= (FieldFigure) figure.frontToBack().next(); // Need way to access
		assertEquals("0: \"test\"", field.getText());
	}

	public void testExploreStringVariable() {
		Object subject= this; // Gratuitous cleverness
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("fName")) {
				each.run();
				break;
			}
		}
		FieldFigure field= (FieldFigure) figure.frontToBack().next(); // Need way to access
		assertEquals("fName: \"testExploreStringVariable\"", field.getText());
	}

	public void testDeleteWithIncomingBinding() {
		Drawing subject= new Drawing();
		//subject.add(new Figure() {public void paint(GC gc) {} public Rectangle bounds() {return new Rectangle(0,0,0,0);}});
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
		figures.next(); // Ignore binding
		ObjectFigure figureOfArrayList= (ObjectFigure) figures.next(); // Need better way to access
		view.remove(figureOfArrayList.getSubject());
		Iterator newFigures= view.getDrawing().frontToBack();
		assertSame(figure, newFigures.next());
		assertTrue(! newFigures.hasNext());
		Iterator figureFigures= figure.frontToBack();
		assertTrue(figureFigures.next() instanceof ClassFigure);
		assertTrue(! figureFigures.hasNext());
	}
	
	public void testDeleteWithOutgoingBinding() {
		Drawing subject= new Drawing();
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
		figures.next(); // Ignore binding
		ObjectFigure figureOfArrayList= (ObjectFigure) figures.next(); // Need way to access
		view.remove(figure.getSubject()); 
		Iterator newFigures= view.getDrawing().frontToBack();
		assertSame(figureOfArrayList, newFigures.next());
		assertTrue(! newFigures.hasNext());
	}
	
	public void testExploreAttribute() {
		Object subject= new Object();
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.attributeActions(view);
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("getClass()")) {
				each.run();
				break;
			}
		}
		view.figureForObject(RawSubject.create(Object.class)); // If it isn't there, this'll blow up
	}

	public void testEnsureBindingListenersAreDeleted() {
		Object subject= new Object();
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.attributeActions(view);
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("getClass()")) {
				each.run();
				break;
			}
		}
		DeleteHandle delete= new DeleteHandle(figure, view);
		delete.run();
		IFigure otherFigure= view.figureForObject(RawSubject.create(Object.class));
		Iterator listeners= otherFigure.getListeners();
		assertEquals(view.getDrawing().getDrawingLayer(), listeners.next());
		assertTrue(! listeners.hasNext()); 
	}

	public void testDeleteHandle() {
		Object subject= new Object();
		ObjectFigure figure= view.exploreObject(subject);
		DeleteHandle delete= new DeleteHandle(figure, view);
		delete.run();
		assertTrue(view.getFigure(new RawSubject(subject), new Point(0,0)) != figure);
	}
	
	public void testExploreSharedObject() {
		Object shared= new Object();
		Object subject= new Object[] {shared, shared};
		ObjectFigure figure= view.exploreObject(subject);
		List items= figure.fieldActions(view);
		for (Iterator all= items.iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			if (each.getText().equals("0")) {
				each.run();
			}
			if (each.getText().equals("1")) {
				each.run();
			}
		}
		ObjectFigure sharedFigure= view.exploreObject(shared);
		view.remove(sharedFigure);
		Iterator figures= view.getDrawing().getDrawingLayer().anyOrder();
		assertEquals(figure, figures.next());
		assertTrue(! figures.hasNext());
	}


}
