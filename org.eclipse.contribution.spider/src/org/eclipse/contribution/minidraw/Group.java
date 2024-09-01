package org.eclipse.contribution.minidraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class Group extends Figure implements IGroup {
	protected List figures= new ArrayList();
	
	public void add(IFigure figure) {
		changed();
		figures.add(0, figure);
		figure.addedTo(this); 
		changed();
	}
	
	public void addAll(Iterator added) {
		while (added.hasNext()) {
			IFigure each= (IFigure) added.next();
			add(each);
		}
	}
	
	public void remove(IFigure removed) {
		changed();
		figures.remove(removed);
		removed.removedFrom(this);
		changed();
	}
	
	public void removeAll() {
		for (Iterator all= figures.iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
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
			if (figure != null) return this; 
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

	public void silentlyMove(int dx, int dy) {
		for (Iterator all= figures.iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			each.silentlyMove(dx, dy);
		}
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
