package org.eclipse.contribution.spider;

import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.ITool;
import org.eclipse.contribution.minidraw.MouseEvent;

public class OpenSourceTool implements ITool {
	protected IFigure figure;

	public OpenSourceTool(IFigure figure) {
		this.figure= figure;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}

	public void mouseUp(MouseEvent e) {
		if (figure instanceof ObjectFigure) {
			((ObjectFigure) figure).showSource();
		}
	}

	public void mouseMove(MouseEvent e) {
	}

}
