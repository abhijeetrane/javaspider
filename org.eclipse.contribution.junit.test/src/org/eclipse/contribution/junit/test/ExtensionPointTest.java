package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.IJavaProject;

public class ExtensionPointTest extends TestCase {
	private static boolean fired;
	public void testNotification() {
		fired= false;
		// The listeners below must be loaded via the extension point
		JUnitPlugin.getPlugin().fireTestsStarted(null, 0);
		assertTrue(fired);
	}
	
	public void testBadListener() {
		fired= false;
		JUnitPlugin.getPlugin().addTestListener(new BadListener());
		JUnitPlugin.getPlugin().fireTestsStarted(null, 0); // Would throw an exception if unprotected
		assertTrue(fired);
	}

	public static class Listener implements ITestRunListener {
		public void testFailed(IJavaProject project, String klass, String method, String trace) {
		}

		public void testsFinished(IJavaProject project) {
		}

		public void testsStarted(IJavaProject project, int testCount) {
			fired= true;
		}

		public void testStarted(IJavaProject project, String klass, String methodName) {
		}

	}

	public static class BadListener implements ITestRunListener {
		public void testFailed(IJavaProject project, String klass, String method, String trace) {
		}

		public void testsFinished(IJavaProject project) {
		}

		public void testsStarted(IJavaProject project, int testCount) {
			throw new NullPointerException();
		}

		public void testStarted(IJavaProject project, String klass, String methodName) {
		}
	}
} 