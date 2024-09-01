package org.eclipse.contribution.minidraw;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public class Ruler implements IRuler {
	private GC gc;
	
	public Ruler(GC gc) {
		this.gc= gc;
	}

	public Point measure(String s) {
		return gc.stringExtent(s);
	}

}
