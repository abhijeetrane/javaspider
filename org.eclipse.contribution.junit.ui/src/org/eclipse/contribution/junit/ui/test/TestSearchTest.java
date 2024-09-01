package org.eclipse.contribution.junit.ui.test;

import java.net.URL;
import java.util.List;

import org.eclipse.contribution.junit.ui.AutoTestBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.testplugin.JavaProjectHelper;

import junit.framework.TestCase;

public class TestSearchTest extends TestCase {
	public TestSearchTest(String name) {
		super(name);
	}

	private IJavaProject project;
	
	public void setUp() throws CoreException {
		project= JavaProjectHelper.createJavaProject("TestProject1", "bin");
		Path relative= new Path("test_resources/junit37-noUI-src.zip");
		URL absolute= Plugin.getDefault().find(relative);
		JavaProjectHelper.addLibrary(project, new Path(absolute.getPath()));
	}

	protected void tearDown() throws Exception {
		//JavaProjectHelper.delete(project);
	}

	public void testFindClass() throws JavaModelException {
		makeClass("Testing", "junit.framework.TestCase");
		List results= (new AutoTestBuilder()).findTests(project);
		assertEquals(1, results.size());
		assertEquals(getClass("Testing"), results.get(0));
	}
	
	private IType getClass(String string) {
		// TODO 
		return null;
	}
	private IJavaProject getProject(String name) {
		return null;
	}
	private void makeClass(String string, String string1) {
		// TODO
	}
	private void makeProject(String string) {
		// TODO
	}
}
