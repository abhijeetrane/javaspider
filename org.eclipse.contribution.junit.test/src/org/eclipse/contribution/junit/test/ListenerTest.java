package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType; 

public class ListenerTest extends TestCase {
	private TestProject project;

	public static class Listener implements ITestRunListener {
		public IJavaProject testFailProject;
		public IJavaProject testStartProject;
		private IJavaProject finishProject;
		private IJavaProject startProject;
		private String trace;
		private String testFailed;
		private String testStarted;
		private boolean started= false;
		private boolean finished= false;
		public void testsStarted(IJavaProject project, int testCount) {
			this.startProject= project;
			started= true;
		}
		public void testsFinished(IJavaProject project) {
			this.finishProject= project;
			finished= true;
		}
		public void testStarted(IJavaProject project, String klass, String method) {
			testStarted= method + " " + klass;
			testStartProject= project;
		}
		public void testFailed(IJavaProject project, String klass, String method, String trace) {
			testFailed= method + " " + klass;
			this.trace= trace;
			testFailProject= project;
		}
	}

	protected void setUp() throws Exception {
		project= new TestProject();
	}

	protected void tearDown() throws Exception {
		project.dispose();
	}

	public void testRunning() throws Exception {
		project.addJar("org.junit", "junit.jar");
		IPackageFragment pack= project.createPackage("pack1");
		IType type= project.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");

		Listener listener= new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		IJavaProject javaProject= project.getJavaProject();
		JUnitPlugin.getPlugin().run(type);
		assertTrue(listener.started);
		assertTrue(listener.finished);
		assertEquals(javaProject, listener.startProject);
		assertEquals(javaProject, listener.finishProject);
		assertEquals(javaProject, listener.testStartProject);
		assertEquals(javaProject, listener.testFailProject);
		assertEquals("testFailure pack1.FailTest", listener.testStarted);
		assertEquals("testFailure pack1.FailTest", listener.testFailed);
		assertTrue(listener.trace.startsWith("junit.framework.AssertionFailedError"));
	}
	
	public void testFailure() throws Exception {
		project.addJar("org.junit", "junit.jar");
		IPackageFragment pack= project.createPackage("pack1");
		IType type= project.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");

		Listener listener= new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		JUnitPlugin.getPlugin().run(type);
		assertEquals("testFailure pack1.FailTest", listener.testFailed);
	}
}
