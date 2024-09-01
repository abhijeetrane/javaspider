package org.eclipse.contribution.spider.test;

import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.swt.graphics.Point;

public class TestRuler implements IRuler {

	public Point measure(String s) {
		return new Point(0,0);
	}

}
