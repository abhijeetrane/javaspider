package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.ui.RerunMarkerResolutionGenerator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IMarkerResolution;

public class MarkerResolutionTest extends TestCase {
	private TestProject testProject;
	private IPackageFragment pack;
	private IType type;

	protected void setUp() throws Exception {
		testProject= new TestProject();
		testProject.addJar("org.junit", "junit.jar");
		pack= testProject.createPackage("pack1");
		type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}public void testOK() {}}");
	}
	
	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testMarkerResolution() throws Exception {
		JUnitPlugin.getPlugin().run(type);
		IMarker[] markers= getFailureMarkers();
		assertEquals(1, markers.length);
		fixProblem();
		RerunMarkerResolutionGenerator generator= new RerunMarkerResolutionGenerator();
		IMarkerResolution[] resolutions= generator.getResolutions(markers[0]);
		resolutions[0].run(markers[0]);
		markers= getFailureMarkers();
		assertEquals(0, getFailureMarkers().length);		
	}
	
	private void fixProblem() throws JavaModelException {
		IMethod testMethod= type.getMethod("testFailure", new String[0]);
		testMethod.delete(true, null);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}

}
