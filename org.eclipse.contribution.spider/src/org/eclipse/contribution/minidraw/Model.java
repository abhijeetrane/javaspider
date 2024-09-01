package org.eclipse.contribution.minidraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Model implements IModel {
	private SelectionDrawing drawing;

	public Model() {
		super();
	}

	public SelectionDrawing getDrawing() {
		if (drawing == null)
			drawing= new SelectionDrawing(this);
		return drawing;
	}

	public void remove(IFigure removed) {
		getDrawing().remove(removed);
	}

	// Later, expand this to full blown contribution lookup by selection class
	// For now, just ask the figure
	public Iterator getHandlesFor(IFigure selection) {
		Iterator handles= handles(selection);
		HandleGroup result= new HandleGroup(selection);
		while (handles.hasNext()) {
			IFigure each= (IFigure) handles.next();
			result.add(each);
		}
		List results= new ArrayList();
		results.add(result);
		return results.iterator();
	}

	private Iterator handles(IFigure selection) {
		List results= new ArrayList();
		results.add(new DeleteHandle(selection, this));
		return results.iterator();
	}

}
