package org.eclipse.contribution.minidraw;

public class HandleTool implements ITool {
	private ITool handle;

	public HandleTool(ITool handle) {
		this.handle= handle;
	}

	public void mouseDown(MouseEvent e) {
		handle.mouseDown(e);
	}

	public void mouseUp(MouseEvent e) {
		handle.mouseUp(e);
	}

	public void mouseMove(MouseEvent e) {
		handle.mouseMove(e);
	}

	public void mouseDoubleClick(MouseEvent e) {
		handle.mouseDoubleClick(e);
	}

}
