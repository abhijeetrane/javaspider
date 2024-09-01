package org.eclipse.contribution.spider;

import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ClassFigure extends Figure {
	protected ISubject subject;
	protected Point where;
	protected Point extent;

	public ClassFigure(ISubject subject, IRuler ruler, Point where) {
		this.subject= subject;
		this.where= where;
		extent= ruler.measure(text());
	}

	public void paint(GC gc) {
		gc.drawString(text(), where.x, where.y);
	}

	public String text() {
		return subject.getClassName();
	}

	public void silentlyMove(int dx, int dy) {
		where.x += dx;
		where.y += dy;
	}

	public Rectangle bounds() {
		return new Rectangle(where.x, where.y, extent.x, extent.y);
	}
}
