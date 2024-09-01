package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.ui.ResultView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ViewColorTest extends TestCase {
	private ResultView view;

	private IWorkbenchPage getPage() {
		IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow window= workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public void setUp() throws Exception {
		String viewID= "org.eclipse.contribution.junit.resultView";
		view= (ResultView) getPage().showView(viewID);
	}

	public void tearDown() throws Exception {
		getPage().hideView(view);
	}

	public void testResultViewGreen() {
		view.getListener().testsStarted(null, 0);
		view.getListener().testsFinished(null);
		Color green= view.getControl().getDisplay().getSystemColor(SWT.COLOR_GREEN);
		assertEquals(green, view.getControl().getBackground());
	}

	public void testResultViewRed() {
		view.getListener().testsStarted(null, 0);
		view.getListener().testFailed(null, "class", "method", "trace");
		Color red= view.getControl().getDisplay().getSystemColor(SWT.COLOR_RED);
		assertEquals(red, view.getControl().getBackground());
		view.getListener().testsFinished(null);
		assertEquals(red, view.getControl().getBackground());
	}

	public void testResultViewColorReset() throws Exception {
		view.getListener().testsStarted(null, 0);
		view.getListener().testFailed(null, "class", "method", "trace");
		view.getListener().testsFinished(null);
		Display display= view.getControl().getDisplay();
		Color red= display.getSystemColor(SWT.COLOR_RED);
		assertEquals(red, view.getControl().getBackground());
		Color background= display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		changeWorkspace();
		assertEquals(background, view.getControl().getBackground());
	}
	
	private void changeWorkspace() throws Exception {
		TestProject project= new TestProject();
		project.createPackage("pack1");
		project.dispose();
	}

	public void testGreenAfterRed() {
		view.getListener().testsStarted(null, 0);
		view.getListener().testFailed(null, "class", "method", "trace");
		view.getListener().testsFinished(null);
		view.getListener().testsStarted(null, 0);
		view.getListener().testsFinished(null);
		Color green= view.getControl().getDisplay().getSystemColor(SWT.COLOR_GREEN);
		assertEquals(green, view.getControl().getBackground());
	}

}
