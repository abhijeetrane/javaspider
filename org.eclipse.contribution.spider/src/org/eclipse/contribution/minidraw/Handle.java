package org.eclipse.contribution.minidraw;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class Handle extends Figure implements IFigureListener, ITool, IHandle {
	protected IFigure target;
	protected IModel model;
	
	public Handle(IFigure target, IModel model) {
		initialize(target, model);
	}

	// Only for use in dynamically creating Handles. You have been warned.
	public Handle() {
	}
	
	public void initialize(IFigure target, IModel model) {
		this.target= target;
		this.model= model;
	}

	public Rectangle bounds() {
		return new Rectangle(target.bounds().x, target.bounds().y, 8, 8);
	}

	public void paint(GC gc) {
		gc.setBackground(gc.getForeground());
		gc.fillRectangle(bounds());
	}

	// ITool
	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
	}

	public void mouseUp(MouseEvent e) {
	}

	public boolean hasTarget(IFigure figure) {
		return target.equals(figure);
	}

}
