package org.eclipse.contribution.junit.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.contribution.junit.JUnitPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class AutoTestBuilder extends IncrementalProjectBuilder {
	public AutoTestBuilder() {
	}

	protected IProject [] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		List tests= findTests();		
		String result= JUnitPlugin.getDefault().runTests(tests);
		System.out.println("Results:" + result);
		return null;
	}

	protected List findTests() throws JavaModelException {
		IJavaProject project= JavaCore.create(getProject());
		return findTests(project);
	}

	public List findTests(IJavaProject project) throws JavaModelException {
		IType root= project.findType("junit.framework.TestCase");
		ITypeHierarchy hierarchy= root.newTypeHierarchy(project, new NullProgressMonitor());
		List tests= new ArrayList();
		IType[] classes= hierarchy.getAllSubtypes(root);
		for (int i= 0; i < classes.length; i++) {
			IType each= classes[i];
			if (Flags.isPublic(each.getFlags()))
				tests.add(each);
		}
		return tests;
	}
}
