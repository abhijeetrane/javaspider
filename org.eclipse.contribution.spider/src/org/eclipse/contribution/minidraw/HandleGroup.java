package org.eclipse.contribution.minidraw;

import java.util.Iterator;

import org.eclipse.swt.graphics.Point;


public class HandleGroup extends Group implements IHandle {
	private IFigure base;
	
	public HandleGroup(IFigure base) {
		initialize(base, null);
	}

	public void initialize(IFigure base, IModel model) {
		this.base= base;
	}

	public void add(IFigure figure) {
		super.add(figure);
		layout();
	}

	private void layout() {
		int x= base.bounds().x;
		for (Iterator all= backToFront(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			each.moveTo(x, base.bounds().y - each.bounds().height);
			x+= each.bounds().width;
		}
	}
	
	// Like Drawing, not Group. Sigh...
	public IFigure figureAt(Point where) {
		for (Iterator all= frontToBack(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			IFigure figure= each.figureAt(where);
			if (figure != null) return figure; 
		}
		return null;
	}

	// If no one is listening to me, I don't have to worry about maintaining the constraints
	public void addListener(IFigureListener listener) {
		if (listeners.isEmpty())
			base.addListener(this);
		super.addListener(listener);
	}

	public void removeListener(IFigureListener listener) {
		super.removeListener(listener);
		if (listeners.isEmpty())
			base.removeListener(this);
	}

	public void changed(IFigure figure) {
		if (figure == base)
			layout();
		else
			super.changed(figure);
	}
	
	public boolean hasTarget(IFigure figure) {
		return base == figure;
	}

}
