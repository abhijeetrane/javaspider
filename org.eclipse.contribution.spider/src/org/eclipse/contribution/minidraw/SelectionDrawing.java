package org.eclipse.contribution.minidraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class SelectionDrawing extends Figure implements IGroup {
	protected IGroup drawing;
	protected IGroup handles;
	protected List selections= new ArrayList();
	protected IModel model;
	protected List figures= new ArrayList();
	
	private void basicAdd(IFigure figure) {
		figures.add(0, figure);
		figure.addedTo(this);
	}

	public SelectionDrawing(IGroup drawing, IModel model) {
		this.drawing= drawing;
		basicAdd(drawing);
		this.handles= new Drawing();
		basicAdd(handles);
		this.model= model;
	}

	public SelectionDrawing() {
		this(new Drawing(), new Model());
	}

	public SelectionDrawing(IModel model) {
		this(new Drawing(), model);
	}

	// This ugliness suggests that the design isn't right yet
	public IFigure figureAt(Point where) {
		for (Iterator all= basicFrontToBack(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			IFigure figure= each.figureAt(where);
			if (figure != null) return figure; 
		}
		return null;
	}

	// Delegated public methods
	public void add(IFigure figure) {
		drawing.add(figure);
	}

	public void remove(IFigure figure) {
		drawing.remove(figure);
		selections.remove(figure);
		for (Iterator all= handlesFor(figure); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			handles.remove(each);
		}
	}

	private Iterator handlesFor(IFigure figure) {
		List result= new ArrayList();
		for (Iterator all= handles.anyOrder(); all.hasNext();) {
			IHandle each= (IHandle) all.next();
			if (each.hasTarget(figure))
				result.add(each);
		}
		return result.iterator();
	}

	public Iterator frontToBack() {
		return drawing.frontToBack();
	}
	
	private Iterator basicFrontToBack() {
		return figures.iterator();
	}

	// Selection
	public void select(IFigure selection) {
		deselect();
		selections.add(selection);
		handles.addAll(model.getHandlesFor(selection));
	}

	public List getSelections() {
		return selections;
	}
	
	public void deselect() {
		selections= new ArrayList();
		handles.removeAll();
	}

	public IGroup getHandleLayer() {
		return handles;
	}

	public IGroup getDrawingLayer() {
		return drawing;
	}

	public void addAll(Iterator added) {
		while (added.hasNext()) {
			IFigure each= (IFigure) added.next();
			add(each);
		}
	}

	public Iterator anyOrder() {
		return frontToBack();
	}

	public Iterator backToFront() {
		return new Reversed(figures);
	}

	public Rectangle bounds() {
		Rectangle result= null;
		for (Iterator all= anyOrder(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			result= (result == null)
				? each.bounds()
				: result.union(each.bounds());
		}
		return result;
	}

	public void paint(GC gc) {
		for (Iterator all= backToFront(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			each.paint(gc);		
		}
	}

	public void removeAll() {
		while (! figures.isEmpty()) {
			IFigure each= (IFigure) figures.get(0);
			remove(each);
		}
	}

}
