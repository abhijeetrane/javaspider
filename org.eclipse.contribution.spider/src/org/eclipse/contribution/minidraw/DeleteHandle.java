package org.eclipse.contribution.minidraw;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class DeleteHandle extends Handle {
	private int x;
	private int y;

	public DeleteHandle() {
	}
	
	public DeleteHandle(IFigure target, IModel model) {
		super(target, model);
	}
	
	public Rectangle bounds() {
		return new Rectangle(x, y, 10, 10);
	}
	
	public void paint(GC gc) {
		int old= gc.getLineWidth();
		gc.setLineWidth(2);
		Rectangle bounds= bounds();
		gc.drawLine(bounds.x, bounds.y, bounds.x + 9, bounds.y + 9);
		gc.drawLine(bounds.x, bounds.y+ 9, bounds.x + 9, bounds.y);
		gc.setLineWidth(old);
	}
	
	public void mouseUp(MouseEvent e) {
		run();
	}

	public void run() {
		model.remove(target);
	}
	public void silentlyMoveTo(int x, int y) {
		this.x= x;
		this.y= y;
	}

}
