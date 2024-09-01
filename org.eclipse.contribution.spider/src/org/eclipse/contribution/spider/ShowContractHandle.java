package org.eclipse.contribution.spider;

import org.eclipse.contribution.minidraw.Handle;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.contribution.minidraw.MouseEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class ShowContractHandle extends Handle {
	IModel model;
	
	public ShowContractHandle() {
	}
	
	public ShowContractHandle(BindingFigure target, IModel model) {
		super(target, model);
		this.model= model;
	}
	
	BindingFigure getBindingFigure() {
		return (BindingFigure) target;
	}
	
	public Rectangle bounds() {
		BindingFigure bf= getBindingFigure();
		int cx= (bf.fromX()+bf.toX())/2;
		int cy= (bf.fromY()+bf.toY())/2;
		return new Rectangle(cx-3, cy - 3, 6, 6);
	}
	
	public void paint(GC gc) {
		Color old= gc.getBackground();
		Rectangle bounds= bounds();
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
		gc.fillRectangle(bounds);
		gc.setBackground(old);
	}
	
	public void mouseUp(MouseEvent e) {
		run();
	}

	public void run() {
		BusyIndicator.showWhile(Display.getDefault(),
			new Runnable() {
				public void run() {
					getBindingFigure().collectCallsOnField(model);
				}
			}
		);
	}
	
	public IFigure figureAt(Point where) {
		return super.figureAt(where);
	}
}
