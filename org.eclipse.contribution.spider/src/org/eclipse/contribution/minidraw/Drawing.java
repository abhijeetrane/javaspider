package org.eclipse.contribution.minidraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class Drawing extends Figure implements IGroup {
	protected List figures= new ArrayList();
	
	public void add(IFigure figure) {
		figures.add(0, figure);
		figure.addedTo(this);
	}
	
	public void addAll(Iterator added) {
		while (added.hasNext()) {
			IFigure each= (IFigure) added.next();
			add(each);
		}
	}
	
	public void remove(IFigure removed) {
		figures.remove(removed);
		removed.removedFrom(this);
	}

	public void removeAll() {
		while (! figures.isEmpty()) {
			IFigure each= (IFigure) figures.get(0);
			remove(each);
		}
	}

	public void paint(GC gc) {
		for (Iterator all= backToFront(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			each.paint(gc);		
		}
	}
	
	public IFigure figureAt(Point where) {
		for (Iterator all= frontToBack(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			IFigure figure= each.figureAt(where);
			if (figure != null) return figure; 
		}
		return null;
	}

	public Iterator frontToBack() {
		return figures.iterator();
	}

	public Iterator backToFront() {
		return new Reversed(figures);
	}
	
	public Iterator anyOrder() {
		return frontToBack();
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

}
