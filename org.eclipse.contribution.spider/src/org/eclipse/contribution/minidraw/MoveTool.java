package org.eclipse.contribution.minidraw;

public class MoveTool implements ITool {
	protected MouseEvent previous;
	protected IFigure figure;
	protected IModel model;
	
	public MoveTool(IFigure figure, IModel model) {
		this.figure= figure;
		this.model= model;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		previous= e;
		model.getDrawing().select(figure);
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
		int dx= e.x - previous.x;
		int dy= e.y - previous.y;
		previous= e;
		figure.move(dx, dy);
	}
	
}
