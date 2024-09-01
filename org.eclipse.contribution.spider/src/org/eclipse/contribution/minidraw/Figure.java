package org.eclipse.contribution.minidraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

abstract public class Figure implements IFigure {
	protected List listeners= new ArrayList();
	
	public void addListener(IFigureListener listener) {
		listeners.add(listener);
	}

	public void removeListener(IFigureListener listener) {
		listeners.remove(listener);
	}
	
	public Iterator getListeners() {
		return listeners.iterator();
	}

	public void changed(IFigure changed) {
		broadcastChange(changed);
	} 

	public void broadcastChange(IFigure figure) {
		for (Iterator all= getListeners(); all.hasNext();) {
			IFigureListener each= (IFigureListener) all.next();
			each.changed(figure);
		}
	}

	public void changed() {
		changed(this);
	}

	abstract public Rectangle bounds();

	abstract public void paint(GC gc);
	
	public void move(int x, int y) {
		changed();
		silentlyMove(x, y);
		changed();
	}
	
	public void silentlyMove(int x, int y) {
	}
	
	public void moveTo(int x, int y) {
		changed();
		silentlyMoveTo(x, y);
		changed();
	}

	public void silentlyMoveTo(int x, int y) {
	}
	
	public IFigure figureAt(Point where) {
		return (bounds().contains(where.x, where.y)) 
			? this
			: null;
	}

	public void addedTo(IGroup drawing) {
		addListener(drawing);
		changed();
	}
	
	public void removedFrom(IGroup drawing) {
		changed();
		removeListener(drawing);
	}
}
