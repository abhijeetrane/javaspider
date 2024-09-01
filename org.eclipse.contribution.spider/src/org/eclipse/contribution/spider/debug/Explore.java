package org.eclipse.contribution.spider.debug;

import org.eclipse.contribution.spider.SpiderView;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

public class Explore implements IObjectActionDelegate {
	private ISelection selection;
	private IWorkbenchPart part;

	/**
	 * Make the expression view visible or open one
	 * if required.
	 */
	protected void showSpiderView() {
		IWorkbenchPage page = JDIDebugUIPlugin.getActivePage();
		if (page != null) {
			IViewPart part = page.findView("org.eclipse.contribution.spider.debug.view");
			if (part == null) {
				try {
					page.showView("org.eclipse.contribution.spider.debug.view");
				} catch (PartInitException e) {
					reportError(e.getStatus().getMessage());
				}
			} else {
				page.bringToTop(part);
			}
		}
	}

	private void reportError(String string) {
		System.out.println(string);
	}

	private SpiderView getSpider() {
		IWorkbenchPage page = JDIDebugUIPlugin.getActivePage();
		return (SpiderView) page.findView("org.eclipse.contribution.spider.debug.view");
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		showSpiderView();
		IJavaVariable subject= (IJavaVariable) ((IStructuredSelection) selection).getFirstElement();
		try {
			getSpider().getFigure(Subject.create((IJavaValue) subject.getValue()), new Point(20, 20));
		} catch (DebugException e) {
			System.out.println("Cannot explore: " + e);
		} // Where ought to be the responsibility of the SpiderView
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection= selection;
	}

}
