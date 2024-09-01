package org.eclipse.contribution.junit.test;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class BuilderTest extends TestCase {

	private TestProject testProject;
	private IPackageFragment pack;
	private IType type;

	protected void setUp() throws Exception {
		testProject= new TestProject();
		testProject.addJar("org.junit", "junit.jar");
	}

	protected void tearDown() throws Exception {
		testProject.dispose();
	}

	public void testAutoTesting() throws Exception {
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				JUnitPlugin.getPlugin().addAutoBuildNature(testProject.getProject());
				pack= testProject.createPackage("pack1");
				type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
		// Don't explicitly run the tests, they should be run automatically
		IMarker[] markers= getFailureMarkers();
		assertEquals(1, markers.length);
	}

	public void testAutoTestingFilter() throws Exception {
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				JUnitPlugin.getPlugin().addAutoBuildNature(testProject.getProject());
				IFile file= testProject.getProject().getFile(new Path("test.exclusions"));
				file.create(new ByteArrayInputStream("pack1.FailTest".getBytes()), true, null);
				pack= testProject.createPackage("pack1");
				type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
		IMarker[] markers= getFailureMarkers();
		assertEquals(0, markers.length);
	}

	public void testBuilderDoesntRunOnCompileError() throws Exception {
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				JUnitPlugin.getPlugin().addAutoBuildNature(testProject.getProject());
				pack= testProject.createPackage("pack1");
				type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { blah blah blah}}");
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
		// Don't explicitly run the tests, they should be run automatically
		IMarker[] markers= getFailureMarkers();
		assertEquals(0, markers.length);
	}
	
	public void testNatureAddAndRemove() throws CoreException {
		JUnitPlugin.getPlugin().addAutoBuildNature(testProject.getProject());
		assertTrue(hasAutoTestNature());
		ICommand[] commands= testProject.getProject().getDescription().getBuildSpec();
		boolean found= false;
		for (int i = 0; i < commands.length; ++i)
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER))
				found= true;
		assertTrue(found);
		JUnitPlugin.getPlugin().removeAutoBuildNature(testProject.getProject());
		assertFalse(testProject.getProject().hasNature(JUnitPlugin.AUTO_TEST_NATURE));
	}

	private boolean hasAutoTestNature() throws CoreException {
		return testProject.getProject().hasNature(JUnitPlugin.AUTO_TEST_NATURE);
	}

	private IMarker[] getFailureMarkers() throws CoreException {
		IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		return root.findMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE);
	}
	
}
