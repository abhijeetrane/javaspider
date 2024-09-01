package org.eclipse.contribution.minidraw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class DrawingCanvas extends Canvas implements PaintListener, IFigureListener {
	protected IRuler ruler;
	protected IModel model;
	protected DrawingController controller; 

	public DrawingCanvas(Composite parent, IModel model) {
		this(parent, model, new WaitTool(model));
	}
	
	public DrawingCanvas(Composite parent, IModel model, ITool tool) {
		super(parent, SWT.NONE);
		this.model= model;
		controller= new DrawingController(model, tool);
		addPaintListener(this);
		addMouseListener(controller);
		addMouseMoveListener(controller);
		setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}

	// Implement PaintListener
	public void paintControl(PaintEvent e) {
		e.gc.fillRectangle(e.x, e.y, e.width, e.height);
		getDrawing().paint(e.gc);
	}

	// Implement FigureListener
	public void changed(IFigure figure) {
		Rectangle bounds= figure.bounds();
		redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}

	public IGroup getDrawing() {
		return model.getDrawing();
	}
	
}
