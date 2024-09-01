package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.ui.ResultView;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ViewTest extends TestCase {
	private ResultView view;

	private IWorkbenchPage getPage() {
		IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow window= workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public void testViewListener() throws PartInitException {
		int count= JUnitPlugin.getPlugin().getListeners().size();
		showView();
		try {
			assertEquals(count + 1, JUnitPlugin.getPlugin().getListeners().size());
		} finally {
			hideView();
		}
		assertEquals(count, JUnitPlugin.getPlugin().getListeners().size());
	}

	public void showView() throws PartInitException {
		view= (ResultView) getPage().showView("org.eclipse.contribution.junit.resultView");
	}

	public void testShowHide() throws PartInitException {
		showView();
		hideView();
	}

	public void hideView() {
		getPage().hideView(view);
	}

}
