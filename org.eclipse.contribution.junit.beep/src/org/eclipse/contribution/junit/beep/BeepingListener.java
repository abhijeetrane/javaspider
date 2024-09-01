package org.eclipse.contribution.junit.beep;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Display;

public class BeepingListener implements ITestRunListener {
	public void testsStarted(IJavaProject project, int testCount) {
	}
	public void testsFinished(IJavaProject project) {
	}
	public void testStarted(IJavaProject project, String klass, String methodName) {
	}
	public void testFailed(IJavaProject project, String klass, String method, String trace) {	
		final Display display= Display.getDefault();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					display.beep();
			}
		});
	}
}
