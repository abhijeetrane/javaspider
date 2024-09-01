package org.eclipse.contribution.minidraw.test;

import junit.framework.TestCase;

import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.HandleGroup;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IFigureListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class HandleGroupTest extends TestCase implements IFigureListener {
	public HandleGroupTest(String name) {
		super(name);
	}

	public void testLayoutAbove() {
		IFigure base= new Figure() {
			private int x= 20;
			private int y= 30;
			public Rectangle bounds() {
				return new Rectangle(x, y, 10, 15);
			}

			public void paint(GC gc) {
			}
			
			public void move(int x, int y) {
				this.changed();
				this.x+= x;
				this.y+= y;
				this.changed();
			}
		};
		HandleGroup layout= new HandleGroup(base);
		IFigure left= new Figure() { 
			private int x;
			private int y;
			public Rectangle bounds() {
				return new Rectangle(x, y, 5, 7);
			}

			public void silentlyMoveTo(int x, int y) {
				this.x= x;
				this.y= y;
			}

			public void paint(GC gc) {
			}
		};
		layout.add(left);
		IFigure right= new Figure() {
			private int x;
			private int y;
			public Rectangle bounds() {
				return new Rectangle(x, y, 8, 9);
			}

			public void silentlyMoveTo(int x, int y) {
				this.x= x;
				this.y= y;
			}

			public void paint(GC gc) {
			}
		};
		layout.add(right);
		
		layout.addListener(this); // Fake the HandleGroup into thinking it's in a drawing
		checkConstraints(base, left, right);
		base.move(1, 1);
		checkConstraints(base, left, right);
	}

	public void checkConstraints(IFigure base, IFigure left, IFigure right) {
		assertEquals(base.bounds().x, left.bounds().x);
		assertEquals(base.bounds().y - left.bounds().height, left.bounds().y);
		assertEquals(base.bounds().x + left.bounds().width, right.bounds().x);
		assertEquals(base.bounds().y - right.bounds().height, right.bounds().y);
		assertEquals(left.bounds().y + left.bounds().height, right.bounds().y + right.bounds().height); // Bottoms are aligned
	}
	public void changed(IFigure changed) {
	}
}
