package org.eclipse.contribution.minidraw.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.DeleteHandle;
import org.eclipse.contribution.minidraw.DrawingController;
import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.Handle;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.contribution.minidraw.MouseEvent;
import org.eclipse.contribution.minidraw.SelectionDrawing;
import org.eclipse.contribution.minidraw.WaitTool;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class InteractionTest extends TestCase implements IModel {
	private SelectionDrawing drawing;
	private DrawingController controller;
	private TestFigure figure;
	private Handle handle;
	
	public InteractionTest(String name) {
		super(name);
	}
	
	class TestFigure extends Figure {
			List handles= new ArrayList();
			public void paint(GC gc) {
			}
			public Rectangle bounds() {
				return new Rectangle(10, 10, 10, 10);
			}
			void setHandles(Handle handle) {
				handles.add(handle);
			}
	}
	
	public void setUp() {
		drawing= new SelectionDrawing(this);
		figure= new TestFigure();
		drawing.add(figure);
		controller= new DrawingController(this, new WaitTool(this));
	}
	
	public void testClickNothing() {
		controller.mouseDown(at(0,0));
		assertTrue(drawing.getSelections().isEmpty());
	}

	public void testClickFigure() {
		handle= new Handle(figure, null);
		controller.mouseDown(at(15, 15));
		assertTrue(! drawing.getSelections().isEmpty());
		assertEquals(handle, drawing.getHandleLayer().frontToBack().next());
		Iterator handleListeners= handle.getListeners();
		assertEquals(drawing.getHandleLayer(), handleListeners.next());
		assertTrue(! handleListeners.hasNext());
	}

	public void testClickFigureThenNothing() {
		handle= new Handle(figure, null);
		controller.mouseDown(at(15, 15));
		controller.mouseDown(at(0, 0));
		assertTrue(drawing.getSelections().isEmpty());
		assertTrue(! drawing.getHandleLayer().frontToBack().hasNext());
		assertTrue(! handle.getListeners().hasNext());
		Iterator figureListeners= figure.getListeners();
		assertEquals(drawing.getDrawingLayer(), figureListeners.next());
		assertTrue(! figureListeners.hasNext());
	}
	
	public void testSelectTwice() {
		handle= new Handle(figure, null);
		controller.mouseDown(at(15, 15));
		controller.mouseDown(at(0, 0));
		controller.mouseDown(at(15, 15));
		controller.mouseDown(at(0, 0));
		assertTrue(drawing.getSelections().isEmpty());
		assertTrue(! drawing.getHandleLayer().frontToBack().hasNext());
		assertTrue(! handle.getListeners().hasNext());
		Iterator figureListeners= figure.getListeners();
		assertEquals(drawing.getDrawingLayer(), figureListeners.next());
		assertTrue(! figureListeners.hasNext());
	}
	
	public void testDeleteHandle() {
		handle= new DeleteHandle(figure, this);
		controller.mouseDown(at(15, 15)); // Select it
		controller.mouseUp(at(15, 15));
		controller.mouseDown(at(handle.bounds().x, handle.bounds().y));
		controller.mouseUp(at(handle.bounds().x, handle.bounds().y));
		assertTrue(drawing.getSelections().isEmpty());
		assertTrue(! drawing.getDrawingLayer().frontToBack().hasNext()); // Drawing is empty
		assertTrue(! drawing.getHandleLayer().frontToBack().hasNext()); // Handles are empty, too
		// This is where we are failing. Have to remove handles. Then get rid of stupid Delete menu item.
	}
	
	private MouseEvent at(int x, int y) {
		return new MouseEvent(x, y);
	}

	public SelectionDrawing getDrawing() {
		return drawing;
	}

	public void remove(IFigure removed) {
		drawing.remove(removed);
	}

	public Iterator getHandlesFor(IFigure selection) {
		List handles= new ArrayList();
		handles.add(handle);
		return handles.iterator();
	}

}
