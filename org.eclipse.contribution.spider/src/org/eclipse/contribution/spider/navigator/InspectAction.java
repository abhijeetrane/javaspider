package org.eclipse.contribution.spider.navigator;

import org.eclipse.jface.action.Action;

class InspectAction extends Action {
	private final SpiderNavigatorView fView;

	InspectAction(SpiderNavigatorView aView) {
		fView= aView;
	}
    
	public void run() {
		fView.fViewer.getControl().setCapture(true);
		fView.getSite().getWorkbenchWindow().getShell().setCursor(fView.getInspectCursor());
		fView.fGrab= true;
	}
}