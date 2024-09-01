package org.eclipse.contribution.spider;

import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class FieldFigure extends Figure {
	protected ISubject subject; // duplicated from containing ObjectFigure. not good
	protected Point where;
	protected Point extent;
	protected IField field;
	protected boolean expanded;
	protected String text;

	public FieldFigure(ISubject subject, IRuler ruler, Point where, IField field) {
		this.subject= subject;
		this.field= field;
		this.where= where;
		text= field.getName();
		extent= ruler.measure(getText());
	}

	public ISubject getSubject() {
		return subject;
	}

	public void paint(GC gc) {
		gc.drawString(getText(), where.x, where.y);
	}

	public void silentlyMove(int dx, int dy) {
		where.x += dx;
		where.y += dy;
	}

	public void silentlyMoveTo(int x, int y) {
		where.x = x; 
		where.y = y;
	}

	public Rectangle bounds() {
		return new Rectangle(where.x, where.y, extent.x, extent.y);
	}

	public ISubject getValue() {
		return field.getValue();
	}

	public void expand(SpiderView spider) {
		if (shouldExpandInline()) // Are there other types that should be expanded inline?
			expandInline(spider.getRuler());
		else
			spider.expandVariable(this, getValue());
	}

	public boolean shouldExpandInline() {
		return field.isNull() || field.isPrimitive() || field.isString();
	}

	private void expandInline(IRuler ruler) {
		String valueString= field.getText();
		text= text + ": " + valueString;
		extent= ruler.measure(getText());
		changed();
	}

	public String getText() {
		return text;
	}

}
