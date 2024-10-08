package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class MarkerTest extends TestCase {

	private TestProject testProject;

	private IPackageFragment pack;

	private IType type;

	protected void setUp() throws Exception {
		testProject= new TestProject();
		testProject.addJar("org.junit", "junit.jar");
		pack= testProject.createPackage("pack1");
		type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");
	}

	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testErrorMarker() throws Exception {
		JUnitPlugin.getPlugin().run(type);

		IMarker marker= getFailureMarkers()[0];
		IMethod method= type.getMethods()[0];
		int start= method.getSourceRange().getOffset();
		assertEquals(start, marker.getAttribute(IMarker.CHAR_START, 0));
		int end= start + method.getSourceRange().getLength();
		assertEquals(end, marker.getAttribute(IMarker.CHAR_END, 0));
		assertTrue(marker.isSubtypeOf(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER));
		assertEquals(IMarker.SEVERITY_ERROR, marker.getAttribute(IMarker.SEVERITY, -1));
	}

	public void testMarkerClearing() throws Exception {
		JUnitPlugin.getPlugin().run(type);
		JUnitPlugin.getPlugin().run(type);

		IMarker[] markers= getFailureMarkers();
		assertEquals(1, markers.length);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}

}
