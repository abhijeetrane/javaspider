package org.eclipse.contribution.minidraw;

public class WaitTool implements ITool {
	private IModel model;
	
	public WaitTool(IModel model) {
		this.model= model;
	}
	
	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		model.getDrawing().deselect();
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
		//This would be a good place to trigger tool tips
	}
}
