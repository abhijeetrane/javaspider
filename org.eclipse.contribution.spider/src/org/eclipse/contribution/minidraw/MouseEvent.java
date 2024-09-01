package org.eclipse.contribution.minidraw;

// We need an event we can easily fake for testing purposes
public class MouseEvent {
	public int x;
	public int y;
	public int stateMask;

	public MouseEvent(org.eclipse.swt.events.MouseEvent e) {
		x= e.x;
		y= e.y;
		stateMask= e.stateMask;
	}

	public MouseEvent(int x, int y) {
		this.x= x;
		this.y= y;
		stateMask= 0;
	}


}
