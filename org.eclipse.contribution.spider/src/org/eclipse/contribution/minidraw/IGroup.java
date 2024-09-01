package org.eclipse.contribution.minidraw;

import java.util.Iterator;

public interface IGroup extends IFigure, IFigureListener {
	void add(IFigure figure);
	void remove(IFigure figure);
	Iterator anyOrder();
	Iterator frontToBack();
	void removeAll();
	void addAll(Iterator iterator);

}
