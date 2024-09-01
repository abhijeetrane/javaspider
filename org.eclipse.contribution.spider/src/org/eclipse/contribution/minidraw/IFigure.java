package org.eclipse.contribution.minidraw;

import java.util.Iterator;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public interface IFigure {
	
	void paint(GC gc);
	Rectangle bounds();
	
	IFigure figureAt(Point where);

	void removedFrom(IGroup group);
	void addedTo(IGroup group);

	Iterator getListeners();
	void addListener(IFigureListener listener);
	void removeListener(IFigureListener listener);

	void changed();

	void move(int x, int y);
	void silentlyMove(int x, int y);
	void moveTo(int x, int y);
	void silentlyMoveTo(int x, int y);

}
