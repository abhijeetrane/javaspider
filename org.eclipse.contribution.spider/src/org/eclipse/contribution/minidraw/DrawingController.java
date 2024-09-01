package org.eclipse.contribution.minidraw;

import org.eclipse.contribution.spider.OpenSourceTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

public class DrawingController implements MouseListener, MouseMoveListener {
	protected IModel model;
	protected ITool tool;
	
	public DrawingController(IModel model, ITool initial) {
		this.model= model;
		this.tool= initial;
	}

	public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
		mouseDown(new MouseEvent(e));
	}
	
	public void mouseDown(MouseEvent e) { 
		IFigure figure= model.getDrawing().figureAt(where(e));
		if (figure == null)
			tool= new WaitTool(model);
		else if (figure instanceof ITool)
			tool= new HandleTool((ITool) figure);
		else if ((e.stateMask & SWT.CTRL) != 0) // TODO
			tool= new OpenSourceTool(figure);		
		else
			tool= new MoveTool(figure, model);
		tool.mouseDown(e);
	}

	public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
		mouseUp(new MouseEvent(e));
	}
	
	public void mouseUp(MouseEvent e) {
		tool.mouseUp(e);
		tool= new WaitTool(model);
	}

	public void mouseMove(org.eclipse.swt.events.MouseEvent e) {
		mouseMove(new MouseEvent(e));
	}
	
	public void mouseMove(MouseEvent e) {
		tool.mouseMove(e);
	}

	public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
		mouseDoubleClick(new MouseEvent(e));
	}
	
	public void mouseDoubleClick(MouseEvent e) {
	}
	
	// These are our MouseEvents now, so we don't have to do this
	private Point where(MouseEvent e) {
		return new Point(e.x, e.y);
	}
}
